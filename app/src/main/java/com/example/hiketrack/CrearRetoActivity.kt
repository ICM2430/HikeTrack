package com.example.hiketrack

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TimeFormatException
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityCrearRetoBinding
import com.example.hiketrack.model.Reto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CrearRetoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearRetoBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            if (it != null) {
                imageUri = it
                loadImage(it)
            }
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearRetoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference

        binding.picture.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.saveChallengeBtn.setOnClickListener {
            val nombre = binding.nombreReto.text.toString()
            val descripcion = binding.descReto.text.toString()
            val fechaInicioTexto = binding.fechaInicio.text.toString()
            val fechaFinTexto = binding.fechaFin.text.toString()

            if (nombre.isEmpty() || descripcion.isEmpty() || fechaInicioTexto.isEmpty() || fechaFinTexto.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val fechaInicio = LocalDate.parse(
                    fechaInicioTexto,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
                val fechaFin = LocalDate.parse(
                    fechaFinTexto,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
                val hoy = LocalDate.now()

                if (fechaInicio.isBefore(hoy)) {
                    Toast.makeText(this, "La fecha de inicio debe ser posterior al día de hoy", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (fechaInicio.isAfter(fechaFin)) {
                    Toast.makeText(this, "La fecha de inicio debe ser anterior a la fecha final", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val generatedRetoId = database.child("retos").push().key
                if (generatedRetoId == null) {
                    Toast.makeText(this, "Error al generar el ID del reto", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (imageUri != null) {
                    subirImagenYPublicar(
                        nombre,
                        descripcion,
                        fechaInicio.toString(),
                        fechaFin.toString(),
                        retoId = generatedRetoId
                    )
                } else {
                    guardarRetoEnBaseDeDatos(
                        nombre,
                        descripcion,
                        fechaInicio.toString(),
                        fechaFin.toString(),
                        imageUrl = "",
                        retoId = generatedRetoId
                    )
                }
            } catch (e: Exception) {


                Toast.makeText(
                    this,
                    "Error antes de publicar: $e",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("RETOS", "Error al recuperar publicaciones: ${e.message}")
            }
        }

        binding.cancelChallengeBtn.setOnClickListener {
            startActivity(Intent(this, RetosActivity::class.java))
        }
    }

    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.picture.setImageBitmap(bitmap)
    }

    private fun subirImagenYPublicar(
        titulo: String,
        descripcion: String,
        fechaInicio: String,
        fechaFin: String,
        retoId: String
    ) {
        val fileName = "$retoId.jpg"
        val imageRef = storageRef.child("retos/$fileName")

        imageUri?.let {
            imageRef.putFile(it).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    guardarRetoEnBaseDeDatos(
                        titulo,
                        descripcion,
                        fechaInicio,
                        fechaFin,
                        imageUrl = uri.toString(),
                        retoId = retoId
                    )
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarRetoEnBaseDeDatos(
        nombre: String,
        descripcion: String,
        fechaInicio: String,
        fechaFin: String,
        imageUrl: String,
        retoId: String
    ) {
        val reto = Reto(
            nombre = nombre,
            descripcion = descripcion,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            participantes = mutableListOf(),
            imageUrl = imageUrl
        )

        database.child("retos").child(retoId).setValue(reto)
            .addOnSuccessListener {
                Toast.makeText(this, "Reto creado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar reto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

