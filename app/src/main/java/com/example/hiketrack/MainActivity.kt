package com.example.hiketrack

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hiketrack.databinding.ActivityMainBinding
import com.example.hiketrack.services.PublicacionNotificationService // Asegúrate de cambiar este nombre si el servicio tiene otro nombre.

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            comenzarServicioNotificaciones()
        } else {
            Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar listeners de los botones
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Verificar permisos y comenzar servicio
        verificarPermisoYServicio(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun verificarPermisoYServicio(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(
                    this,
                    "Por favor acepte las notificaciones para recibir alertas de publicaciones.",
                    Toast.LENGTH_LONG
                ).show()
            }
            notificationPermissionLauncher.launch(permission)
        } else {
            comenzarServicioNotificaciones()
        }
    }

    private fun comenzarServicioNotificaciones() {
        val intent = Intent(this, PublicacionNotificationService::class.java) // Cambia por el nombre correcto de tu servicio.
        startService(intent)
    }
}
