package com.example.hiketrack

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityCrearRetoBinding

class CrearRetoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearRetoBinding
    val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            loadImage(it!!)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearRetoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsButton.setOnClickListener{
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        binding.picture.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        binding.saveChallengeBtn.setOnClickListener{
            val intent = Intent (this, RetosEnCursoActivity::class.java)
            startActivity(intent)
        }

        binding.cancelChallengeBtn.setOnClickListener{
            val intent = Intent (this, RetosActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadImage(uri: Uri) {
        val imageStream = getContentResolver().openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.picture.setImageBitmap(bitmap)
    }
}