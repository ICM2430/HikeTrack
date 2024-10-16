package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityConfiguracionBinding

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.configuracionCuenta.setOnClickListener {
            startActivity( Intent(this, ConfiguracionCuentaActivity::class.java))
        }
        binding.configuracionSeguridad.setOnClickListener {
            startActivity( Intent(this, ConfiguracionSeguridadActivity::class.java))
        }

    }
}