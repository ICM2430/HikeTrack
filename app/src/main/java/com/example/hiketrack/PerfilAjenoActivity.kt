package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hiketrack.databinding.ActivityPerfilAjenoBinding
import com.example.hiketrack.model.Usuario
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilAjenoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

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

        binding.inicioButton.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }

        binding.retosButton.setOnClickListener {
            val intent = Intent(this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.recorridosButton.setOnClickListener {
            val intent = Intent(this, RecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.perfilButton.setOnClickListener {
            val intent = Intent(this, EstadisticasActivity::class.java)
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
}
