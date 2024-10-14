package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityCrearRetoBinding

class CrearRetoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearRetoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearRetoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsButton.setOnClickListener{
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.saveChallengeBtn.setOnClickListener{
            val intent = Intent (this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.cancelChallengeBtn.setOnClickListener{
            val intent = Intent (this, RetosActivity::class.java)
            startActivity(intent)
        }

    }
}