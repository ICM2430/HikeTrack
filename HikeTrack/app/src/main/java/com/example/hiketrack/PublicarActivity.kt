package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityPublicarBinding

class PublicarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}