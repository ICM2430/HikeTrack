package com.example.hiketrack

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hiketrack.adapters.PublicacionAdapter
import com.example.hiketrack.databinding.ActivityPerfilAjenoBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.example.hiketrack.model.Publicacion
import com.example.hiketrack.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PerfilAjenoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilAjenoBinding
    private lateinit var database: DatabaseReference
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private val publicaciones = mutableListOf<Publicacion>()
    private lateinit var adapter: PublicacionAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilAjenoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        database = FirebaseDatabase.getInstance().reference

        adapter = PublicacionAdapter(this, publicaciones)
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.iniciarConversacion.setOnClickListener {
            val usuarioConversadoId = intent.getStringExtra("userId") ?: return@setOnClickListener

            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val database = FirebaseDatabase.getInstance().reference

            // Verificar si ya existe un chat
            val chatsRef = database.child("chats")
            chatsRef.orderByChild("usuarios/$currentUserId").equalTo(true)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var chatId: String? = null

                        for (chatSnapshot in snapshot.children) {
                            val usuarios = chatSnapshot.child("usuarios").value as? Map<*, *>
                            if (usuarios?.containsKey(usuarioConversadoId) == true) {
                                chatId = chatSnapshot.key
                                break
                            }
                        }

                        if (chatId == null) {
                            chatId = chatsRef.push().key

                            // Crear el objeto Chat para guardar
                            val newChat = hashMapOf(
                                "usuarios" to mapOf(
                                    currentUserId to true,
                                    usuarioConversadoId to true
                                ),
                                "ultimoMensaje" to null
                            )


                            chatsRef.child(chatId!!).setValue(newChat)
                                .addOnSuccessListener {
                                    Log.i("Firebase", "Chat creado con éxito")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firebase", "Error al crear chat: ${e.message}")
                                }
                        }

                        // Agregar usuarioConversado a la lista de conversados
                        val currentUserRef = database.child("users").child(currentUserId).child("usuariosConversados")
                        currentUserRef.child(usuarioConversadoId).setValue(true)

                        val conversadoRef = database.child("users").child(usuarioConversadoId).child("usuariosConversados")
                        conversadoRef.child(currentUserId).setValue(true)

                        // Abrir la actividad de chat
                        val intent = Intent(this@PerfilAjenoActivity, ChatActivity::class.java)
                        intent.putExtra("chatId", chatId)
                        startActivity(intent)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Error al buscar chats: ${error.message}")
                    }
                })
        }

        val dividerDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.custom_divider)
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable)
        }

        binding.feedRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.feedRecyclerView.adapter = adapter
        cargarPublicaciones()

        val userId = intent.getStringExtra("userId")
        if (userId != null) {
            cargarDatosUsuario(userId)
        } else {
            Log.e("PerfilAjenoActivity", "No se recibió un userId en el Intent")
        }

        // Configuración de botones de navegación
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun cargarDatosUsuario(userId: String) {
        val userRef = database.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("usuario").getValue(String::class.java)
                    val imagenPerfil = snapshot.child("imagenPerfil").getValue(String::class.java)

                    // Actualiza el nombre del perfil
                    binding.nombrePerfil.text = username ?: "Sin nombre"

                    // Intenta cargar la imagen desde Firebase Storage
                    loadImageForUser(userId)
                } else {
                    Toast.makeText(this@PerfilAjenoActivity, "Datos no encontrados", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PerfilAjenoActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadImageForUser(userId: String) {
        val possibleExtensions = listOf("jpg", "png", "jpeg") // Extensiones soportadas
        tryLoadingImageForUser(userId, possibleExtensions, 0)
    }

    private fun tryLoadingImageForUser(userId: String, extensions: List<String>, index: Int) {
        if (index >= extensions.size) {
            // No se encontró ninguna imagen en los formatos especificados
            binding.imagenPerfil.setImageResource(R.drawable.cancel) // Imagen de fallback
            return
        }

        val currentExtension = extensions[index]
        val profileImageRef = storageRef.child("images/$userId.$currentExtension")

        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            // Si se encuentra la imagen, cargarla en el espacio del perfil
            Glide.with(this@PerfilAjenoActivity)
                .load(uri)
                .into(binding.imagenPerfil)
        }.addOnFailureListener {
            // Si falla, intenta con el siguiente formato
            tryLoadingImageForUser(userId, extensions, index + 1)
        }
    }

    private fun cargarPublicaciones() {
        val userId = intent.getStringExtra("userId") // Obtiene el userId del usuario ajeno desde el Intent
        if (userId == null) {
            Log.e("PerfilAjenoActivity", "No se proporcionó un userId en el Intent")
            return
        }

        database.child("publicaciones").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val publicacionesActualizadas = mutableListOf<Publicacion>()
                for (childSnapshot in snapshot.children) {
                    val publicacion = childSnapshot.getValue(Publicacion::class.java)
                    publicacion?.id = childSnapshot.key

                    // Filtrar solo las publicaciones que pertenecen al usuario ajeno
                    if (publicacion != null && publicacion.userId == userId) {
                        publicacionesActualizadas.add(publicacion)
                    }
                }
                Log.e("PerfilAjenoActivity", "Número de publicaciones cargadas del usuario ajeno: ${publicacionesActualizadas.size}")

                publicaciones.clear()
                publicaciones.addAll(publicacionesActualizadas)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al recuperar publicaciones: ${error.message}")
            }
        })
    }

    private fun iniciarConversacion(userId: String) {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid == null) {
            Toast.makeText(this, "No se ha iniciado sesión.", Toast.LENGTH_SHORT).show()
            return
        }

        // Referencia al usuario actual en la base de datos
        val currentUserRef = database.child("users").child(currentUserUid)
        val observedUserRef = database.child("users").child(userId)

        // Obtener datos del usuario observado
        observedUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Crear objeto Usuario del usuario observado
                    val observedUser = snapshot.getValue(Usuario::class.java)
                    if (observedUser != null) {
                        // Actualizar la lista de usuarios conversados del usuario actual
                        currentUserRef.child("usuariosConversados").child(userId).setValue(observedUser)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@PerfilAjenoActivity,
                                    "Conversación iniciada con ${observedUser.nombre}.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@PerfilAjenoActivity,
                                    "Error al iniciar conversación.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(this@PerfilAjenoActivity, "Usuario no encontrado.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@PerfilAjenoActivity,
                    "Error al obtener datos del usuario.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}
