package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.estadisticas.setOnClickListener {
            val intent =  Intent(this,EstadisticasActivity::class.java)
            startActivity(intent)
        }

        binding.seleccionarRecorrido.setOnClickListener {
            val intent =  Intent(this,RecorridosActivity::class.java)
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

        binding.flotante.setOnClickListener {
            val intent = Intent(this, PublicarActivity::class.java)
            startActivity(intent)
        }
    }
}