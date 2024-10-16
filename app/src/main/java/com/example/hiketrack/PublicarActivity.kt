package com.example.hiketrack

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityPublicarBinding
import java.io.File

class PublicarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicarBinding
    lateinit var uriCamera : Uri

    val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            loadImage(it!!)
        }
    )

    val getContentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
        ActivityResultCallback {
            if(it){
                loadImage(uriCamera)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.publishBtn.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        binding.cancelPublishBtn.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }

        binding.galleryButton.setOnClickListener{
            galleryLauncher.launch("image/*")
        }
        binding.cameraButton.setOnClickListener{
            val file = File(getFilesDir(), "picFromCamera");
            uriCamera = FileProvider.getUriForFile(baseContext, baseContext.packageName +
                    ".fileprovider", file)
            getContentCamera.launch(uriCamera)
        }

    }

    private fun loadImage(uri: Uri) {
        val imageStream = getContentResolver().openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.image.setImageBitmap(bitmap)
    }
}


