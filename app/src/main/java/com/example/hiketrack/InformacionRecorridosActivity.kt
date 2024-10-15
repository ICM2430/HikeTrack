package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ActivityInformacionRecorridosBinding

class InformacionRecorridosActivity : AppCompatActivity() {

    lateinit var binding: ActivityInformacionRecorridosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacionRecorridosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.IniciarRecorridoButton.setOnClickListener {
            val intent = Intent(this, MapsTrackerActivity::class.java)
            startActivity(intent)
        }
    }
}