package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up onClickListeners for the icons
        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        binding.searchIcon.setOnClickListener {
            val intent = Intent(this, BuscarPerfilActivity::class.java)
            startActivity(intent)
        }

        // Set up bottom navigation item selection
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, FeedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_stats -> {
                    val intent = Intent(this, EstadisticasActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_challenges -> {
                    val intent = Intent(this, SeleccionarRecorrido::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_trophies -> {
                    val intent = Intent(this,Retos::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this,Contactos::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

        binding.flotante.setOnClickListener {
            val intent = Intent(this, PublicarActivity::class.java)
            startActivity(intent)
        }
    }
}