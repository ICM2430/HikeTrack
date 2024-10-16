package com.example.hiketrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityRetosBinding
import android.content.Intent

class RetosActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRetosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRetosBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDisponibles.setOnClickListener{
            val intent = Intent(this, RetosActivity::class.java)
            startActivity(intent)
        }

        binding.btnEnCurso.setOnClickListener{
            val intent = Intent(this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.flotante.setOnClickListener {
            val intent = Intent (this, CrearRetoActivity::class.java)
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