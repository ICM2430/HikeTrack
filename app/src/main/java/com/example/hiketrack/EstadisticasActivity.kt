package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityEstadisticasBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EstadisticasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstadisticasBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadisticasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            cargarDatosUsuario(uid)
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.settingsButton.setOnClickListener {
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.chatButton.setOnClickListener {
            val intent = Intent(this, ContactosActivity::class.java)
            startActivity(intent)
        }

        binding.perfilEstadisticasBtn.setOnClickListener {
            val intent = Intent (this, EstadisticasActivity::class.java)
            startActivity(intent)
        }

        binding.perfilPublicacionesBtn.setOnClickListener {
            val intent = Intent (this, PerfilActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarDatosUsuario(uid: String) {
        val userRef = database.child("users").child(uid)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("usuario").getValue(String::class.java)
                    val followers = snapshot.child("seguidores").getValue(Int::class.java) ?: 0
                    val following = snapshot.child("seguidos").getValue(Int::class.java) ?: 0
                    val imagenPerfil = snapshot.child("imagenPerfil").getValue(String::class.java)

                    Log.d("FIREBASE_DATA", "Nombre: $username")
                    Log.d("FIREBASE_DATA", "Seguidores: $followers")
                    Log.d("FIREBASE_DATA", "Seguidos: $following")


                    // Actualiza el nombre del usuario
                    binding.nombrePerfil.text = username ?: "Sin nombre"

                    // Actualiza seguidores y seguidos
                    binding.numeroSeguidores.text = followers?.toString() ?: "0"
                    binding.numeroSeguidos.text = following.toString() ?: "0"

                    loadImageForCurrentUser(uid)
                } else {
                    Toast.makeText(this@EstadisticasActivity, "Datos no encontrados", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EstadisticasActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadImageForCurrentUser(userId: String) {
        val possibleExtensions = listOf("jpg", "png", "jpeg") // Lista de extensiones soportadas
        tryLoadingImageForCurrentUser(userId, possibleExtensions, 0)
    }

    private fun tryLoadingImageForCurrentUser(userId: String, extensions: List<String>, index: Int) {
        if (index >= extensions.size) {
            // No se encontró ninguna imagen en los formatos especificados
            binding.imagenPerfil.setImageResource(R.drawable.cancel) // Imagen de fallback
            return
        }

        val currentExtension = extensions[index]
        val profileImageRef = storageRef.child("images/$userId.$currentExtension")

        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            // Si se encuentra la imagen, cargarla en el espacio del perfil
            Glide.with(this@EstadisticasActivity)
                .load(uri)
                .into(binding.imagenPerfil)
        }.addOnFailureListener {
            // Si falla, intenta con el siguiente formato
            tryLoadingImageForCurrentUser(userId, extensions, index + 1)
        }
    }

}