package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Subrayar el TextView de "Iniciar sesión" usando View Binding
        val iniciarSesionText = SpannableString("Iniciar sesión")
        iniciarSesionText.setSpan(UnderlineSpan(), 0, iniciarSesionText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.createAccountTextView.text = iniciarSesionText

        // Configurar eventos de clic
        binding.registerButton.setOnClickListener {
            // Crear un Intent para ir a FeedActivity
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }

        binding.createAccountTextView.setOnClickListener {
            // Crear un Intent para ir a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}