package com.example.hiketrack

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiketrack.adapters.PublicacionAdapter
import com.example.hiketrack.databinding.ActivityFeedBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.example.hiketrack.model.Publicacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var database: DatabaseReference
    private val publicaciones = mutableListOf<Publicacion>()
    private lateinit var adapter: PublicacionAdapter
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        database = FirebaseDatabase.getInstance().reference.child("publicaciones")
        auth = FirebaseAuth.getInstance()


        adapter = PublicacionAdapter(this, publicaciones)
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(this)

        val dividerDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.custom_divider)
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable)
        }

        binding.feedRecyclerView.addItemDecoration(dividerItemDecoration)

        binding.feedRecyclerView.adapter = adapter

        cargarPublicaciones()

        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.flotante.setOnClickListener {
            val intent = Intent(this, PublicarActivity::class.java)
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
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val publicacionesActualizadas = mutableListOf<Publicacion>()
                for (childSnapshot in snapshot.children) {
                    val publicacion = childSnapshot.getValue(Publicacion::class.java)
                    publicacion?.id = childSnapshot.key // Incluye el ID asignado por Firebase
                    if(publicacion != null && auth.currentUser!!.uid != publicacion.userId){
                        publicacionesActualizadas.add(publicacion)
                    }
                }
                publicaciones.clear()
                publicaciones.addAll(publicacionesActualizadas) // Actualiza la lista local de publicaciones
                adapter.notifyDataSetChanged() // Notifica cambios al adaptador
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al recuperar publicaciones: ${error.message}")
            }
        })
    }
}