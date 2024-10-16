package com.example.hiketrack

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityConfiguracionCuentaBinding
import com.example.hiketrack.databinding.ActivityConfiguracionSeguridadBinding

class ConfiguracionCuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionCuentaBinding
    val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            loadImage(it!!)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.picture.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

    }
    private fun loadImage(uri: Uri) {
        val imageStream = getContentResolver().openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.picture.setImageBitmap(bitmap)
    }
}