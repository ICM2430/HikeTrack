package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityTrackerRecorridoBinding

class TrackerRecorridoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackerRecorridoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackerRecorridoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonEmergencia.setOnClickListener {
            val intent = Intent(this, EmergenciaActivity::class.java)
            startActivity(intent)
        }

        binding.botonFinalizarRecorrido.setOnClickListener {
            val intent = Intent(this, CalificarRecorridoActivity::class.java)
            startActivity(intent)
        }
    }
}
