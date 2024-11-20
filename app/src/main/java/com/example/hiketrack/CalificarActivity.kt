package com.example.hiketrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ActivityCalificarBinding
import com.example.hiketrack.model.MyLocation
import com.example.hiketrack.model.Recorrido

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.io.File
import java.util.Date


class CalificarActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCalificarBinding

    var recorridoPublicar = Recorrido()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalificarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        leerArchivo()

        binding.finalizarButton.setOnClickListener {
            val intent = Intent(this, RecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.compartirButton.setOnClickListener {

            //to be changed
            val intent = Intent(this, RecorridosActivity::class.java)
            startActivity(intent)
        }

        binding.favoritoButton.setOnClickListener {

            //to be changed
            val intent = Intent(this, RecorridosActivity::class.java)
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

    // Leer el archivo locations.json y guardar la info en la variable recorridoPublicar

    fun leerArchivo() {

        val fileName = "locations.json"
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            try {
                val jsonString = file.readText()
        val json = JSONObject(jsonString)
        recorridoPublicar.nombre = json.getString("nombre")
        recorridoPublicar.elevacion = json.getString("elevacion")
        recorridoPublicar.calificacion = json.getInt("calificacion")
        recorridoPublicar.distancia = json.getDouble("distancia").toFloat()
        recorridoPublicar.tiempoEstimado = json.getInt("tiempoEstimado")
        val locations = json.getJSONArray("myLocations")
        for (i in 0 until locations.length()) {
            val location = locations.getJSONObject(i)
            val date = getDate(location, "date")
            recorridoPublicar.myLocations.add(
                MyLocation(
                    location.getDouble("latitude"),
                    location.getDouble("longitude"),
                    date
                )
            )
        }

                Log.d("Recorrido", recorridoPublicar.tiempoEstimado.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CalificarActivity", "Error reading file: $fileName", e)
            }
        } else {
            Log.e("CalificarActivity", "File not found: $fileName")
        }
    }

    fun getDate(json: JSONObject, clave: String): Date {
        val tiempoEnMilisegundos = json.getLong(clave)
        return Date(tiempoEnMilisegundos)
    }


}