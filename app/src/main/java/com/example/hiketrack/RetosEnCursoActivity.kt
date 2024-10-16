package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityRetosEnCursoBinding

class RetosEnCursoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetosEnCursoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetosEnCursoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsButton.setOnClickListener {
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.flotante.setOnClickListener {
            val intent = Intent (this, CrearRetoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDisponibles.setOnClickListener {
            val intent = Intent (this, RetosActivity::class.java)
            startActivity(intent)
        }

        binding.btnEnCurso.setOnClickListener {
            val intent = Intent (this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.inicioButton.setOnClickListener {
            val intent =  Intent(this,FeedActivity::class.java)
            startActivity(intent)
        }

        binding.retosButton.setOnClickListener {
            val intent =  Intent(this,RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.recorridosButton.setOnClickListener {
            val intent =  Intent(this,RecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.perfilButton.setOnClickListener {
            val intent =  Intent(this,EstadisticasActivity::class.java)
            startActivity(intent)
        }
    }
}