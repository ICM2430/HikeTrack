package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiketrack.adapters.MensajeAdapter
import com.example.hiketrack.databinding.ActivityChatBinding
import com.example.hiketrack.model.Mensaje
import com.example.hiketrack.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val mensajes =  mutableListOf<Mensaje>()
    private lateinit var adapter: MensajeAdapter
    private lateinit var currentUser: Usuario
    private lateinit var otherUserId: String
    private lateinit var chatId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        chatId = intent.getStringExtra("chatId") ?: return
        otherUserId = intent.getStringExtra("otherUserId") ?: return

        binding.recyclerViewMensajes.layoutManager = LinearLayoutManager(this)

        Log.e("CHAT", "LLEGO 1")


        cargarUsuarioActual { user ->
            currentUser = user

            adapter = MensajeAdapter(mensajes, currentUser)
            binding.recyclerViewMensajes.adapter = adapter

            cargarMensajes()
        }

        binding.btnEnviar.setOnClickListener {
            val contenido = binding.editMensaje.text.toString().trim()
            if (contenido.isNotEmpty()) {
                enviarMensaje(contenido)
            } else {
                Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun cargarUsuarioActual(callback: (Usuario) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return
        database.child("users").child(currentUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Log para verificar los datos obtenidos
                    val usuarioMap = snapshot.value as? Map<String, Any>
                    Log.d("FirebaseDebug", "Datos obtenidos: $usuarioMap")

                    try {
                        // Intentar deserializar el objeto Usuario
                        val usuario = snapshot.getValue(Usuario::class.java)
                        if (usuario != null) {
                            callback(usuario) // Llamar al callback con el usuario deserializado
                        } else {
                            Log.e("ChatActivity", "Usuario no encontrado o estructura inválida")
                        }
                    } catch (e: Exception) {
                        Log.e("ChatActivity", "Error al deserializar usuario: ${e.message}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error al cargar usuario: ${error.message}")
                }
            })
    }


    private fun cargarMensajes() {
        Log.e("CHAT", "LLEGO 3")

        database.child("chats").child(chatId).child("mensajes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("CHAT", "LLEGO 4")

                    mensajes.clear()
                    for (mensajeSnapshot in snapshot.children) {
                        val mensaje = mensajeSnapshot.getValue(Mensaje::class.java)
                        mensaje?.let { mensajes.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                    binding.recyclerViewMensajes.scrollToPosition(mensajes.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error al cargar mensajes: ${error.message}")
                }
            })
    }


    private fun enviarMensaje(contenido: String) {
        val mensaje = Mensaje().apply {
            remitente = currentUser
            receptor = Usuario().apply { usuario = otherUserId }
            this.contenido = contenido
            fechaEnvio = System.currentTimeMillis().toString() // Cambiar por un formato adecuado si es necesario
        }

        database.child("chats").child(chatId).child("mensajes").push()
            .setValue(mensaje)
            .addOnSuccessListener {
                binding.editMensaje.text.clear()
                Log.d("ChatActivity", "Mensaje enviado: $contenido")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al enviar mensaje: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}