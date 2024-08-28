package com.example.hiketrack

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
    }
}