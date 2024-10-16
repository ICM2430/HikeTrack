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

        binding.home.setOnClickListener {
            val intent =  Intent(this,FeedActivity::class.java)
            startActivity(intent)
        }

        binding.estadisticas.setOnClickListener {
            val intent =  Intent(this,EstadisticasActivity::class.java)
            startActivity(intent)
        }

        binding.seleccionarRecorrido.setOnClickListener {
            val intent =  Intent(this,SeleccionarRecorrido::class.java)
            startActivity(intent)
        }

        binding.contactos.setOnClickListener {
            val intent =  Intent(this,ContactosActivity::class.java)
            startActivity(intent)
        }
    }
}