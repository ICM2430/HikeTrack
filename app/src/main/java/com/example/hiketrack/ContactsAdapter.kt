package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityContactsAdapterBinding

class ContactsAdapter : AppCompatActivity() {
    private lateinit var binding: ActivityContactsAdapterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}