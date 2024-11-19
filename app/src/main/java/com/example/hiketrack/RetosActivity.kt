package com.example.hiketrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityRetosBinding
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiketrack.adapters.RetoAdapter
import com.example.hiketrack.decorations.GridSpacingItemDecoration
import com.example.hiketrack.model.Reto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RetosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRetosBinding
    private lateinit var database: DatabaseReference
    private val retos = mutableListOf<Reto>()
    private lateinit var adapter: RetoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("retos")

        adapter = RetoAdapter(retos)


        binding.retosRecyclerView.layoutManager = GridLayoutManager(
            this, // Contexto
            2,    // Número de columnas
            GridLayoutManager.VERTICAL,
            false
        )
        binding.retosRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(16) // Espaciado en píxeles
        )


        binding.retosRecyclerView.adapter = adapter

        cargarRetos()

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

    private fun cargarRetos() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                retos.clear()
                for (retoSnapshot in snapshot.children) {
                    val reto = retoSnapshot.getValue(Reto::class.java)
                    reto?.id = retoSnapshot.key // Asigna el ID generado por Firebase
                    reto?.let { retos.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al cargar retos: ${error.message}")
            }
        })
    }
}