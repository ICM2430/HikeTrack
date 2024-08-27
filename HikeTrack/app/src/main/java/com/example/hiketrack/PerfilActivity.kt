package com.example.hiketrackpantallas2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrackpantallas2.databinding.ActivityPublicarBinding

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicarBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_perfil)

    }
}