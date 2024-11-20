package com.example.hiketrack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ActivityMapsTrackerBinding
import com.example.hiketrack.model.MyLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Date
import kotlin.math.ln
import kotlin.math.sqrt
import com.example.hiketrack.model.Recorrido



class MapsTrackerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsTrackerBinding

    // sensor

    // Giroscopio
    private lateinit var sensorManager: SensorManager
    private var rotationSensor: Sensor? = null
    private val rotationListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    if (azimuth < 0) {
                        azimuth += 360
                    }
                    binding.rotacion.text = String.format("%.1f°", azimuth)
                }
            }
        }
    }


    private var lightSensor : Sensor? = null
    private var pressureSensor: Sensor? = null
    private var accelerometer: Sensor? = null

    private lateinit var sensorEventListener: SensorEventListener
    private lateinit var geocoder: Geocoder


    // bonuspack

    private lateinit var roadManager: RoadManager

    private var roadOverlay: Polyline? = null


    //CurrentLocation

    private var currentLocation: LatLng = LatLng(0.0, 0.0) // Valor por defecto


    //JSONObject
    private var locations = ArrayList<JSONObject>()

    //fuseLocationClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //Variables de presion y altura
    private var pressureReference: Float = -1f
    private val temperature = 288.15 // Temperatura media aproximada en Kelvin (15°C)
    private val g = 9.80665

    //Rastreo actividad fisica
    private var activityState: String = "Unknown"


    //subscripcion a localizacion

    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private lateinit var locationCallback: LocationCallback


    // manejo del tiempo

    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var elapsedTime = 0L

    //pasos
    private var stepCounterSensor: Sensor? = null
    private var stepCount = 0

    //distancia
    private var totalDistance: Int = 0

    private var currentMarker: com.google.android.gms.maps.model.Marker? = null


    // OnCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sensor de luz
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        rotationSensor?.let {
            sensorManager.registerListener(rotationListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
        sensorEventListener = createSensorEventListener()

        // Barometro
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        //Acelerometro
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        //Geocoder
        geocoder = Geocoder(baseContext)





        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // permission for location
        locationPermissionRequest(Manifest.permission.ACCESS_FINE_LOCATION)
        locationPermissionRequest(Manifest.permission.ACCESS_COARSE_LOCATION)


        // roadManager

        roadManager = OSRMRoadManager(this, "ANDROID")

        //Pruebas

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // fusedLocationClient

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // LocationRequest
        locationRequest = createLocationRequest()


        //locationCallback
        locationCallback = createLocationCallBack()

        binding.IniciarRecorrido.setOnClickListener {
            iniciarRecorrido()
            binding.IniciarRecorrido.visibility = android.view.View.GONE
            binding.blockActionScreen.visibility = android.view.View.GONE
        }

        //locationSettings

        val locationSettings =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult(),
                ActivityResultCallback {
                    if(it.
                        resultCode ==
                        RESULT_OK){
                        startLocationUpdates()
                    }else{
                        Toast.makeText(this, "Location is not enabled", Toast.LENGTH_SHORT).show()
                    }
                })


        /*
                // rutas

                binding.rutas.setOnClickListener {
                    drawRouteFromFile()
                }


                // Search address

                binding.address.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        val address = binding.address.text.toString()
                        val location = findLocation(address)
                        if (location != null){
                            drawMarker(location,address, R.drawable.baseline_location_black)
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(18f))

                            getCurrentLocation { NewcurrentLocation ->
                                if (NewcurrentLocation != null) {
                                    drawRoute(NewcurrentLocation, location)
                                } else {
                                    Toast.makeText(this, "No se pudo obtener localizacion", Toast.LENGTH_SHORT).show()
                                }
                            }


                        }
                    }
                    true
                }*/


    }



    //Permision for location

    private fun locationPermissionRequest(permission: String){
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(permission)){
                Toast.makeText(this, "The permission is needed because...", Toast.LENGTH_LONG).show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        }
    }


    // Funcion que al presionar el boton incie el rastreo de la ubicacion

    fun iniciarRecorrido (){




        // contador de tiempo

        startTime = System.currentTimeMillis()
        runnable = object : Runnable {
            override fun run() {
                elapsedTime = System.currentTimeMillis() - startTime

                // Convertir el tiempo transcurrido a segundos, minutos y horas
                val seconds = (elapsedTime / 1000) % 60
                val minutes = (elapsedTime / (1000 * 60)) % 60
                val totalMinutes = elapsedTime / (1000 * 60)
                val hours = elapsedTime / (1000 * 60 * 60)

                // Formatear el tiempo dependiendo del valor de minutos u horas
                val formattedTime = when {
                    totalMinutes < 60 -> {
                        // Mostrar minutos si son menos de 60 minutos
                        "$totalMinutes min"
                    }
                    else -> {
                        // Mostrar horas con decimales si es mayor o igual a 60 minutos
                        val decimalHours = hours + minutes / 60.0
                        String.format("%.1f h", decimalHours)
                    }
                }

                // Actualizar el TextView con el tiempo formateado usando binding
                binding.tiempo.text = formattedTime


                // Llamar a run() de nuevo en un intervalo de 1 segundo
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)

        // mover a pantalla de emergencia

        binding.emergencybutton.setOnClickListener {
            val intent = Intent(this, EmergencyActivity::class.java)
            startActivity(intent)
        }

        //mover a calificar
        binding.finalizarbutton.setOnClickListener {
            val intent = Intent(this, CalificarActivity::class.java)
            startActivity(intent)
        }

        lightSensor?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            Log.e("Sensor", "Sensor de luz no disponible.")
        }
        accelerometer?.let {
            sensorManager.registerListener(accelerometerEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }?: run {
            Log.e("Sensor", "Acelerometro no disponible.")
        }
        pressureSensor?.let {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        val currentPressure = event.values[0] // Presión actual en hPa

                        if (pressureReference == -1f) {
                            pressureReference = currentPressure
                        } else {
                            val P0 = pressureReference * 100
                            val P = currentPressure * 100

                            val height = (temperature / g) * ln(P0 / P)

                            binding.altitud.text = String.format("%.2f m", height)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                }
            }, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            Log.e("Sensor", "Sensor de presión no disponible.")
        }
    }

    private fun drawRouteFromFile() {
        val fileName = "locations.json"
        val file = File(getExternalFilesDir(null), fileName)



        if (!file.exists()) {
            Toast.makeText(this, "El archivo de localizaciones no existe.", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val jsonContent = file.readText()
            val locationsArray = JSONArray(jsonContent)

            for (i in 0 until locationsArray.length()-1) {
                val locationObjectStart = locationsArray.getJSONObject(i)
                val locationObjectEnd = locationsArray.getJSONObject(i + 1)
                val latitudeStart =  locationObjectStart.getDouble("latitude")
                val longitudeStart = locationObjectStart.getDouble("longitude")
                val latlnStart = LatLng(latitudeStart, longitudeStart)

                val latitudeEnd =  locationObjectEnd.getDouble("latitude")
                val longitudeEnd = locationObjectEnd.getDouble("longitude")
                val latlnEnd = LatLng(latitudeEnd, longitudeEnd)

                drawRoute(latlnStart, latlnEnd)
            }




        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al leer el archivo de localizaciones.", Toast.LENGTH_LONG).show()
        }


    }


    //Funcion que inicia medicion de

    // OnResume and onPause for sensor

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        accelerometer?.let {
            sensorManager.registerListener(accelerometerEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        pressureSensor?.let {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        val currentPressure = event.values[0] // Presión actual en hPa

                        if (pressureReference == -1f) {
                            pressureReference = currentPressure
                        } else {
                            val P0 = pressureReference * 100
                            val P = currentPressure * 100

                            val height = (temperature / g) * ln(P0 / P)

                            binding.altitud.text = String.format("%.2f m", height)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                }
            }, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            Log.e("Sensor", "Sensor de presión no disponible.")
        }
        startLocationUpdates()

    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
        sensorManager.unregisterListener(accelerometerEventListener)
        stopLocationUpdates()
        writeJSONObject()
    }

    // Create sensor event listener

    private fun createSensorEventListener(): SensorEventListener {
        val listener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (this@MapsTrackerActivity::mMap.isInitialized)
                    if(lightSensor != null){
                        if (event != null) {
                            if(event.values[0] < 5000){
                                //darkMode
                                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_dark))
                            }else {
                                //lightMode
                                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_light))
                            }
                        }
                    }
                //sensor pasos
                if (event?.sensor == stepCounterSensor && event != null) {
                    stepCount = event.values[0].toInt()
                    binding.pasos.text = stepCount.toString()
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

        }
        return listener
    }

    private val accelerometerEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                // Calcular la magnitud de la aceleración
                val accelerationMagnitude = sqrt(x * x + y * y + z * z)

                // Detectar actividad con base en la aceleración
                detectActivity(accelerationMagnitude)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }
    private fun detectActivity(accelerationMagnitude: Float) {
        when {
            accelerationMagnitude < 1.5 -> {
                // Si la aceleración es baja, el usuario probablemente está parado
                activityState = "Parado"
            }
            accelerationMagnitude in 1.5..3.0 -> {
                // Aceleración moderada indica que el usuario está caminando
                activityState = "Caminando"
            }
            accelerationMagnitude > 3.0 -> {
                // Alta aceleración indica que el usuario está corriendo o en un vehículo
                activityState = "Corriendo"
            }
        }

        // Actualizar el TextView en la interfaz con el estado de la actividad
        runOnUiThread {
            binding.textViewActivityState.text = activityState
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

        getCurrentLocation { newcurrentLocation ->
            if (newcurrentLocation != null) {
                currentLocation = newcurrentLocation
                mMap.addMarker(MarkerOptions().position(currentLocation).title("Your Current Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

            } else {

            }
        }




        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)







    }


    private fun drawMarker(location : LatLng, description : String?, icon: Int){
        val address = findAddress(location)
        val addressMarker = mMap.addMarker(MarkerOptions().position(location).title(address ?: "Direccion no encontrada").icon(bitmapDescriptorFromVector(this,
            R.drawable.location)))!!
        if(description!=null){
            addressMarker.title=description
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.moveCamera(CameraUpdateFactory.zoomIn())
    }

    private fun bitmapDescriptorFromVector(context : Context, vectorResId : Int) : BitmapDescriptor {
        val vectorDrawable : Drawable = ContextCompat.getDrawable(context, vectorResId)!!
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight())
        val bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun findAddress (location : LatLng):String?{
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 2)
        if(addresses != null && !addresses.isEmpty()){
            val addr = addresses.get(0)
            val locname = addr.getAddressLine(0)
            return locname
        }
        return null
    }

    fun findLocation(address : String):LatLng?{
        val addresses = geocoder.getFromLocationName(address, 2)
        if(addresses != null && !addresses.isEmpty()){
            val addr = addresses.get(0)
            val location = LatLng(addr.latitude, addr.
            longitude)
            return location
        }
        return null
    }

    // Distancia entre posicion actual y marcador

    fun distanceToCurrentPosition(lat1 : Double, long1: Double, lat2:Double, long2:Double) : Unit{
        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(long1 - long2)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)+
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        val result = 6371 * c;
        val toastResult = Math.round(result*100.0)/100.0;

        Toast.makeText(
            this, "Distancia a posicion actual: $toastResult km", Toast.LENGTH_LONG).show()
    }


    // rutas

    // Método para dibujar la ruta
    private fun drawRoute(startLatLng: LatLng, endLatLng: LatLng) {
        // Convertir LatLng a GeoPoint
        val startGeoPoint = GeoPoint(startLatLng.latitude, startLatLng.longitude)
        val endGeoPoint = GeoPoint(endLatLng.latitude, endLatLng.longitude)

        // Crear lista de GeoPoints
        val routePoints = ArrayList<GeoPoint>()
        routePoints.add(startGeoPoint)
        routePoints.add(endGeoPoint)

        // Obtener la ruta utilizando el RoadManager
        val road = roadManager.getRoad(routePoints)


        val routePath = ArrayList<LatLng>()

        // Convertir los puntos de la ruta en la lista de road.mRouteHigh a LatLng para Google Maps
        for (geoPoint in road.mRouteHigh) {
            routePath.add(LatLng(geoPoint.latitude, geoPoint.longitude))
        }

        // Dibujar la polilínea en Google Maps
        mMap.addPolyline(
            com.google.android.gms.maps.model.PolylineOptions()
                .addAll(routePath)
                .color(Color.RED)
                .width(10f)
        )

        // Ajustar la cámara para mostrar toda la ruta
        val boundsBuilder = LatLngBounds.builder()
        boundsBuilder.include(startLatLng)
        boundsBuilder.include(endLatLng)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))



    }



    private fun getCurrentLocation(callback: (LatLng?) -> Unit) {


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    checkAndRecordLocation(currentLocation)
                    callback(currentLocation)
                } else {
                    Toast.makeText(this, "No se pudo obtener ubicacion", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            callback(null)
        }
    }


    // Guardar localizacion en json
    fun writeJSONObject() {
        val myLocation = MyLocation.MyLocation(
            currentLocation.latitude,
            currentLocation.longitude,
            Date(System.currentTimeMillis()),
            totalDistance,
            stepCount,
            elapsedTime
        )

        val filename = "locations.json"
        val file = File(
            baseContext.getExternalFilesDir(null), filename)

        // Si el archivo existe, cargar las ubicaciones previas
        if (file.exists() && file.length() > 0) {
            val jsonContent = file.readText()
            val existingLocations = JSONArray(jsonContent)
            for (i in 0 until existingLocations.length()) {
                locations.add(existingLocations.getJSONObject(i))
            }
        }

        locations.add(myLocation.toJSON())
        val output = BufferedWriter(FileWriter(file))
        output.write(locations.toString())
        output.close()

        Log.i("LOCATION", "File modified at path: ${file}" )
        Log.d("FILE_CONTENT", file.readText())

    }

    // updates

    fun startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
            ACCESS_FINE_LOCATION)
            == PackageManager.
            PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }


    // callback para la localizacion

    fun createLocationCallBack() : LocationCallback{
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                if(result!=null){
                    val newlocation = result.
                    lastLocation!!
                    val newLatLng = LatLng(newlocation.latitude, newlocation.longitude)
                    checkAndRecordLocation(newLatLng)

                    if (currentMarker != null) {
                        currentMarker?.remove()
                    }

                    currentMarker = mMap.addMarker(MarkerOptions().position(currentLocation).title("Your Current Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                    Log.i("LOCATION", "New location: $newLatLng")
                }
            }
        }
        return locationCallback
    }

    // Crear LocationRequest

    fun createLocationRequest() : com.google.android.gms.location.LocationRequest {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.
            PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(5000)
            .build()
        return locationRequest
    }



    // Checar si la posicion actual es mayor a 30 metros de la anterior

    private fun checkAndRecordLocation(newLocation: LatLng) {
        if (currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            val distance = FloatArray(1)
            Location.distanceBetween(
                currentLocation.latitude, currentLocation.longitude,
                newLocation.latitude, newLocation.longitude, distance
            )

            if (distance[0] > 5) {

                //dibuja la ruta recorrida en el mapa
                drawRoute(currentLocation, newLocation)


                currentLocation = newLocation
                totalDistance = totalDistance + distance[0].toInt()
                binding.distancia.text = "${totalDistance} m"
                writeJSONObject()






            }
        } else {
            // Si no hay una ubicación anterior, se guarda la nueva ubicación
            currentLocation = newLocation
            binding.distancia.text = 0.toString()
            writeJSONObject()
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



    // funcion para cargar el estado guardado

    private fun loadSavedState() {

        val fileName = "locations.json"
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            try {
                val jsonContent = file.readText()
                val locationsArray = JSONArray(jsonContent)

                restoreLocations(locationsArray)
                restoreAdditionalData(locationsArray)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("LOAD_SAVED_STATE", e.toString())
            }
        }

    }

    private fun restoreLocations(locationsArray: JSONArray) {
        for (i in 0 until locationsArray.length()) {
            val location = locationsArray.getJSONObject(i)
            val savedLocation = LatLng(location.getDouble("latitude"), location.getDouble("longitude"))

            drawMarker(savedLocation, null, R.drawable.location)

            // Dibuja la ruta solo si no es el primer punto
            if (i > 0) {
                val previousLocation = locationsArray.getJSONObject(i - 1)
                drawRoute(
                    LatLng(previousLocation.getDouble("latitude"), previousLocation.getDouble("longitude")),
                    savedLocation
                )
            }
        }
    }

    private fun restoreAdditionalData(locationsArray: JSONArray) {
        val lastLocationObject = locationsArray.getJSONObject(locationsArray.length() - 1)
        totalDistance = lastLocationObject.getInt("distance")
        stepCount = lastLocationObject.getInt("steps")
        elapsedTime = lastLocationObject.getLong("time")

        binding.distancia.text = "$totalDistance m"
        binding.pasos.text = stepCount.toString()
        startTime = System.currentTimeMillis() - elapsedTime // Para seguir con el cronómetro desde donde se pausó
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(rotationListener)
    }

}