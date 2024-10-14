package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityEstadisticasBinding

class EstadisticasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstadisticasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadisticasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.home.setOnClickListener {
            val intent =  Intent(this,FeedActivity::class.java)
            startActivity(intent)
        }

        binding.seleccionarRecorrido.setOnClickListener {
            val intent =  Intent(this,SeleccionarRecorrido::class.java)
            startActivity(intent)
        }

        binding.botonRetos.setOnClickListener {
            val intent =  Intent(this,RetosActivity::class.java)
            startActivity(intent)
        }

        binding.contactos.setOnClickListener {
            val intent =  Intent(this,Contactos::class.java)
            startActivity(intent)
        }
    }
}