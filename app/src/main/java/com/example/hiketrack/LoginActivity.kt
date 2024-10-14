package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityLoginBinding
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Subrayar el TextView de "Crear cuenta"
        val createAccountText = SpannableString("Crear cuenta")
        createAccountText.setSpan(UnderlineSpan(), 0, createAccountText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.createAccountTextView.text = createAccountText

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java) // Define el intent para FeedActivity
            startActivity(intent)
        }

        // Manejar clics para el texto de crear cuenta
        binding.createAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java) // Define el intent para RegisterActivity
            startActivity(intent)
        }

    }
}