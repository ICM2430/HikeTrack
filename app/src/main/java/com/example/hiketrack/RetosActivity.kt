package com.example.hiketrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityRetosBinding
import android.content.Intent

class RetosActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRetosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRetosBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDisponibles.setOnClickListener{
            val intent = Intent(this, RetosActivity::class.java)
            startActivity(intent)
        }

        binding.btnEnCurso.setOnClickListener{
            val intent = Intent(this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.flotante.setOnClickListener {
            val intent = Intent (this, CrearRetoActivity::class.java)
            startActivity(intent)
        }

    }
}