package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityContactosBinding

class Contactos : AppCompatActivity() {
    private lateinit var binding : ActivityContactosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*

        binding.profileIcon.setOnClickListener {
            val intent =  Intent(this,PerfilActivity::class.java)
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


        binding.listaContactos.setOnClickListener {
            val intent =  Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }
*/
    }
}