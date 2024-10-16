package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivitySeleccionarRecorridoBinding

class SeleccionarRecorrido : AppCompatActivity() {
    private lateinit var binding : ActivitySeleccionarRecorridoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarRecorridoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.perfil.setOnClickListener {
            val intent =  Intent(this,PerfilActivity::class.java)
            startActivity(intent)
        }

        binding.listaRecorridosP.setOnClickListener {
            val intent =  Intent(this,DescripcionRecorridoActivity::class.java)
            startActivity(intent)
        }

        binding.listaRecorridosPa.setOnClickListener {
            val intent =  Intent(this,DescripcionRecorridoActivity::class.java)
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


        binding.contactos.setOnClickListener {
            val intent =  Intent(this,ContactosActivity::class.java)
            startActivity(intent)
        }
    }
}