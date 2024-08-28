package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityRetoBinding

class Reto : AppCompatActivity() {
    private lateinit var binding : ActivityRetoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iniciarRecorrido.setOnClickListener {
            val intent =  Intent(this,TrackerRecorridoActivity::class.java)
            startActivity(intent)
        }
    }
}