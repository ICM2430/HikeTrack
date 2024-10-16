package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ActivityRecorridosBinding

class RecorridosActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecorridosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecorridosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsBtn.setOnClickListener {
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido1.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido2.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido3.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido4.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.nuevorecorrido.setOnClickListener {
            val intent = Intent(this, MapsTrackerActivity::class.java)
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