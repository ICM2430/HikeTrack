package com.example.hiketrack

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.hiketrack.databinding.ActivityPublicarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*

class PublicarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublicarBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null
    private lateinit var uriCamera: Uri

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            it?.let { uri ->
                imageUri = uri
                loadImage(uri)
            }
        }
    )

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
        ActivityResultCallback {
            if (it) {
                imageUri?.let { uri ->
                    loadImage(uri)
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference

        binding.galleryButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.cameraButton.setOnClickListener {
            val file = File(filesDir, "picFromCamera.jpg")
            uriCamera = FileProvider.getUriForFile(
                baseContext, "$packageName.fileprovider", file
            )
            imageUri = uriCamera
            cameraLauncher.launch(uriCamera)
        }

        binding.publishBtn.setOnClickListener {
            val titulo = binding.nombreReto.text.toString()
            val descripcion = binding.descReto.text.toString()

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUri != null) {
                    subirImagenYPublicar(titulo, descripcion)
                } else {
                    guardarPublicacionEnBaseDeDatos(titulo, descripcion, null)
                }
            }
        }

        binding.cancelPublishBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.image.setImageBitmap(bitmap)
    }

    private fun subirImagenYPublicar(titulo: String, descripcion: String) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val imageRef = storageRef.child("publicaciones/$fileName")

        imageUri?.let {
            imageRef.putFile(it).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    guardarPublicacionEnBaseDeDatos(titulo, descripcion, uri.toString())
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarPublicacionEnBaseDeDatos(titulo: String, descripcion: String, imageUrl: String?) {
        val userId = auth.currentUser?.uid ?: return
        val publicacionId = database.child("publicaciones").push().key

        val publicacion = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "imageUrl" to imageUrl,
            "userId" to userId,
            "fecha" to System.currentTimeMillis()
        )

        publicacionId?.let {
            database.child("publicaciones").child(it).setValue(publicacion)
                .addOnSuccessListener {
                    Toast.makeText(this, "Publicación guardada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar publicación: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}


