package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityRecorridosBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RecorridosActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecorridosBinding
    private lateinit var database: FirebaseDatabase
    private val storageRef = FirebaseStorage.getInstance().reference

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




    }

    fun importarRecorridos(){

    }
}