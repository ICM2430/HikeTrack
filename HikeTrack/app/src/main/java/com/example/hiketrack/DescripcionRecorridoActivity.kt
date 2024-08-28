package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityDescripcionRecorridoBinding

class DescripcionRecorridoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescripcionRecorridoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescripcionRecorridoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonIniciarRecorrido.setOnClickListener {
            val intent = Intent(this, TrackerRecorridoActivity::class.java)
            startActivity(intent)
        }


    }
}