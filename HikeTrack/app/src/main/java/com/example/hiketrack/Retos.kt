package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityRetosBinding
import android.content.Intent

class Retos : AppCompatActivity() {
    private lateinit var binding : ActivityRetosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRetosBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.perfil.setOnClickListener {
            val intent =  Intent(this,PerfilActivity::class.java)
            startActivity(intent)
        }

        binding.listaRetosP.setOnClickListener {
            val intent =  Intent(this,Reto::class.java)
            startActivity(intent)
        }

        binding.listaRetosPa.setOnClickListener {
            val intent =  Intent(this,Reto::class.java)
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
            val intent =  Intent(this,Contactos::class.java)
            startActivity(intent)
        }
    }
}