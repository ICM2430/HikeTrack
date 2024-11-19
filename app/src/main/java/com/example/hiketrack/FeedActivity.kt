package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        cargarPublicaciones()


        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.chatButton.setOnClickListener {
            val intent = Intent(this, ContactosActivity::class.java)
            startActivity(intent)
        }

        binding.searchContainer.setOnClickListener {
            val intent = Intent (this, BuscarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarPublicaciones() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                publicaciones.clear()
                for (postSnapshot in snapshot.children) {
                    val publicacion = postSnapshot.getValue(Publicacion::class.java)
                    publicacion?.let { publicaciones.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }
}