package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth


class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        binding.settingsButton.setOnClickListener{
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.chatButton.setOnClickListener {
            val intent = Intent(this, ContactosActivity::class.java)
            startActivity(intent)
        }

        binding.perfilEstadisticasBtn.setOnClickListener {
            val intent = Intent (this, EstadisticasActivity::class.java)
            startActivity(intent)
        }

        binding.perfilPublicacionesBtn.setOnClickListener {
            val intent = Intent (this, PerfilActivity::class.java)
            startActivity(intent)
        }


        binding.logOutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}