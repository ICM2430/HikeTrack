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

        binding.recorrido1.setOnClickListener {
            val intent = Intent(this, DescripcionRecorridoActivity::class.java)
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

    }
}