package com.example.hiketrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class UbicacionTiempoRealActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var database: DatabaseReference
    private var userMarker: Marker? = null
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion_tiempo_real)

        userId = intent.getStringExtra("userId") ?: return

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        database = FirebaseDatabase.getInstance().reference
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        trackUserLocation()
    }

    private fun trackUserLocation() {
        val userLocationRef = database.child("users").child(userId).child("location")
        userLocationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("latitude").getValue(Double::class.java)
                val lng = snapshot.child("longitude").getValue(Double::class.java)
                if (lat != null && lng != null) {
                    val userLatLng = LatLng(lat, lng)
                    if (userMarker == null) {
                        userMarker = mMap.addMarker(MarkerOptions().position(userLatLng).title("User Location"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                    } else {
                        userMarker!!.position = userLatLng
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}