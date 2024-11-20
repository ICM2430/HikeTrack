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
import com.example.hiketrack.adapters.RetoAdapter
import com.example.hiketrack.adapters.RetoEnCursoAdapter
import com.example.hiketrack.databinding.ActivityRetosEnCursoBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.example.hiketrack.model.Reto
import com.example.hiketrack.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RetosEnCursoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRetosEnCursoBinding
    private lateinit var database: DatabaseReference
    private val retos = mutableListOf<Reto>()
    private lateinit var adapter: RetoEnCursoAdapter
    private lateinit var usuarioActual: Usuario
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetosEnCursoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("retos")

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        binding.retosRecyclerView.layoutManager = LinearLayoutManager(this)

        val dividerDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.custom_divider)
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable)
        }

        binding.retosRecyclerView.addItemDecoration(dividerItemDecoration)

        cargarUsuarioActual { usuario ->
            usuarioActual = usuario

            Log.e("RETOS", "Usuario cargado: ${usuario.nombre}")

            // Inicializa el adaptador con el usuario actual
            adapter = RetoEnCursoAdapter(retos, usuarioActual)
            binding.retosRecyclerView.adapter = adapter
            cargarRetos()
        }

        binding.settingsButton.setOnClickListener {
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.flotante.setOnClickListener {
            val intent = Intent (this, CrearRetoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDisponibles.setOnClickListener {
            val intent = Intent (this, RetosActivity::class.java)
            startActivity(intent)
        }

        binding.btnEnCurso.setOnClickListener {
            val intent = Intent (this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

    }

    private fun cargarUsuarioActual(callback: (Usuario) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)



        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    callback(usuario) // Pasar el usuario cargado al callback
                } else {
                    Log.e("Firebase", "Usuario no encontrado en la base de datos")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al cargar usuario: ${error.message}")
            }
        })
    }

    private fun cargarRetos() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val retosActualizados = mutableListOf<Reto>()

                for (retoSnapshot in snapshot.children) {
                    val reto = retoSnapshot.getValue(Reto::class.java)
                    reto?.id = retoSnapshot.key

                    if (reto != null && reto.participantes.any { it.correo == usuarioActual.correo }) {
                        retosActualizados.add(reto)
                    }
                }

                Log.i("RETOS", "Número de retos cargados donde participa el usuario: ${retosActualizados.size}")

                retos.clear()
                retos.addAll(retosActualizados)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al cargar retos: ${error.message}")
            }
        })
    }
}