package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityBuscarPerfilBinding
import com.example.hiketrack.databinding.ActivityConfiguracionBinding

class BuscarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}