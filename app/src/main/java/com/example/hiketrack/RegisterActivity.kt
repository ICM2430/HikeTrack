package com.example.hiketrack

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hiketrack.databinding.ActivityRegisterBinding
import com.example.hiketrack.model.Recorrido
import com.example.hiketrack.model.Reto
import com.example.hiketrack.model.Usuario
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.Manifest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val storageRef = FirebaseStorage.getInstance().reference
    private var selectedImageUri: Uri? = null
    private val TAG = "HIKETRACK_APP"
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    // Lanzador para galería
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            loadImage(uri)
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.AlreadyHaveAccTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.createAccountTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.imageButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.registerButton.setOnClickListener {
            val correo = binding.emailEditText.text.toString()
            val contraseña = binding.passwordEditText.text.toString()
            val nombre = binding.nombreEditText.text.toString()
            val usuario = binding.usernameEditText.text.toString()

            if (validateForm(correo, contraseña, nombre, usuario)) {
                createUser(correo, contraseña, nombre, usuario)
            }
        }
    }

    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.imageButton.setImageBitmap(bitmap)
    }

    private fun validateForm(
        correo: String,
        contraseña: String,
        nombre: String,
        usuario: String
    ): Boolean {
        var valid = true
        if (correo.isEmpty()) {
            binding.emailEditText.error = "Campo requerido"
            valid = false
        }
        if (contraseña.isEmpty()) {
            binding.passwordEditText.error = "Campo requerido"
            valid = false
        } else if (contraseña.length < 6) {
            binding.passwordEditText.error = "La contraseña debe tener al menos 6 caracteres"
            valid = false
        }
        if (nombre.isEmpty()) {
            binding.nombreEditText.error = "Campo requerido"
            valid = false
        }
        if (usuario.isEmpty()) {
            binding.usernameEditText.error = "Campo requerido"
            valid = false
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Seleccione una imagen de perfil", Toast.LENGTH_LONG).show()
            valid = false
        }
        return valid
    }

    private fun createUser(correo: String, contraseña: String, nombre: String, usuario: String) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = mAuth.currentUser
                    if (firebaseUser != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(usuario)
                            .setPhotoUri(selectedImageUri)
                            .build()

                        firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    val newUser = Usuario(
                                        correo = correo,
                                        nombre = nombre,
                                        usuario = usuario
                                    )
                                    saveUserDataToDatabase(firebaseUser.uid, newUser)
                                    uploadImageToStorage(firebaseUser)
                                    navigateToFeedActivity()
                                } else {
                                    Log.e(
                                        TAG,
                                        "Error al actualizar perfil: ${profileTask.exception?.message}"
                                    )
                                    Toast.makeText(
                                        this,
                                        "Error al actualizar perfil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    Log.e(TAG, "Error al crear usuario: ${task.exception?.message}")
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    // RegisterActivity.kt
    private fun saveUserDataToDatabase(uid: String, user: Usuario) {
        user.usuariosConversados = mutableListOf(Usuario("Placeholder", "placeholder", "placeholder@example.com"))
        user.listaRetos = mutableListOf(Reto("Placeholder"))
        user.listaRecorridos = mutableListOf(Recorrido())

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                user.latitude = location.latitude
                user.longitude = location.longitude
            } else {
                user.latitude = 0.0
                user.longitude = 0.0
            }

            database.getReference("users").child(uid).setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Datos guardados en la base de datos")
                    } else {
                        Log.e(TAG, "Error al guardar datos: ${task.exception?.message}")
                    }
                }
        }
    }



    private fun uploadImageToStorage(user: FirebaseUser) {
        if (selectedImageUri == null) {
            Log.e(TAG, "El URI seleccionado es nulo")
            Toast.makeText(this, "Seleccione una imagen para subir", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef1 = FirebaseStorage.getInstance().reference.child("images/${user.uid}.jpg")
        Log.d("ERROR FEO", "Referencia de almacenamiento: ${storageRef1.path}")
        Log.d("ERROR FEO", "UID: ${user.uid}")

        try {
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            if (inputStream == null) {
                Log.e(TAG, "No se pudo acceder al archivo desde el URI")
                Toast.makeText(this, "Error al acceder al archivo seleccionado", Toast.LENGTH_SHORT).show()
                return
            }
            inputStream.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error al verificar el archivo: ${e.message}")
            return
        }


        selectedImageUri?.let { uri ->
            val imageRef = storageRef.child("images/${user.uid}.jpg")
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    Log.d(TAG, "Image uploaded successfully.")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to upload image: ${exception.message}")
                }
        }
    }

    private fun navigateToFeedActivity() {
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }

}

