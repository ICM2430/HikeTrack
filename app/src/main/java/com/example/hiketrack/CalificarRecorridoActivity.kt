package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityCalificarRecorridoBinding

class CalificarRecorridoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalificarRecorridoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalificarRecorridoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonAcabarRecorrido.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }
    }
}