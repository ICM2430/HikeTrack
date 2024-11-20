package com.example.hiketrack

import android.R.id.message
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hiketrack.databinding.ActivityEmergencyBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class EmergencyActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.llamar123button.setOnClickListener {
            val emergencyNumber = "123"

            val intent = Intent(Intent.ACTION_DIAL)
            // toast llamando 123

            intent.data = Uri.parse("tel:$emergencyNumber")
        }

        binding.compartirButton.setOnClickListener {

            // Creating new intent
            val intent = Intent(Intent.ACTION_SEND)

            intent.setType("text/plain")
            intent.setPackage("com.whatsapp")


            // Give your message here
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Compartiendo recorrido wip"
            )


            // Checking whether Whatsapp
            // is installed or not
            if (intent
                    .resolveActivity(
                        packageManager
                    )
                == null
            ) {
                Toast.makeText(
                    this,
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }


            // Starting Whatsapp
            startActivity(intent)
        }

        binding.contactoEmergenciaButton.setOnClickListener {
            val emergencyNumber = "123"

            //cambiar por numero de emergencia

            val intent = Intent(Intent.ACTION_DIAL).apply{
             data = Uri.parse("tel:$emergencyNumber")}

            startActivity(intent)
        }

        binding.cancelbutton.setOnClickListener {
            val intent = Intent(this, MapsTrackerActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}