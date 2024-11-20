package com.example.hiketrack.service

import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

data class TouristPoint(val name: String, val location: LatLng)

class TouristService {

    private val client = OkHttpClient()

    fun getTouristPoints(): List<TouristPoint> {
        val request = Request.Builder()
            .url("http://10.0.2.2:3000/tourist-points")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonArray = JSONArray(response.body?.string())
            val points = mutableListOf<TouristPoint>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val latitude = jsonObject.getDouble("latitude")
                val longitude = jsonObject.getDouble("longitude")
                points.add(TouristPoint(name, LatLng(latitude, longitude)))
            }

            return points
        }
    }
}