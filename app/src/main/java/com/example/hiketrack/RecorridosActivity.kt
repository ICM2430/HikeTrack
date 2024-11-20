package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.hiketrack.databinding.ActivityRecorridosBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.example.hiketrack.model.Recorrido
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage


class RecorridosActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecorridosBinding
    private lateinit var database: FirebaseDatabase
    private val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var myRef: DatabaseReference

    val recorridosRef = "recorridos/"


    //Lista de recorridos
    var recorridos = mutableListOf<Recorrido>()

    lateinit var vel : ValueEventListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecorridosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        binding.settingsBtn.setOnClickListener {
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido1.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido2.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido3.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.recorrido4.setOnClickListener {
            val intent = Intent(this, InformacionRecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.nuevorecorrido.setOnClickListener {
            val intent = Intent(this, MapsTrackerActivity::class.java)
            startActivity(intent)
        }


        val composeView = binding.composeView
        composeView.setContent {
            showRecorridos(recorridos)
        }






    }

    fun importarRecorridos(){
        // Esta funcion importa los recorridos de la base de datos y los guarda en la lista recorridos

        myRef = database.getReference(recorridosRef)
        var vel = myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val recorrido = child.getValue<Recorrido>()

                    if (recorrido != null) {
                        recorridos.add(recorrido)
                    }

                    if (recorrido != null) {
                        Log.i("Recorridos", recorrido.nombre)
                    }

                }

                // adapter.updateUsers(usersList)


            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


    override fun onPause() {
        myRef.removeEventListener(vel)
        super.onPause()
    }
    override fun onStart() {
        super.onStart()

        importarRecorridos()
    }
    
    @Composable
    fun showRecorridos(recorridos: List<Recorrido>) {

        LazyColumn {
            items(recorridos) { recorrido ->
                Text(text = recorrido.nombre)
            }
        }
    }
}