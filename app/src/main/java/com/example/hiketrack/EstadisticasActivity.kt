package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityEstadisticasBinding

class EstadisticasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstadisticasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadisticasBinding.inflate(layoutInflater)
        setContentView(binding.root)

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