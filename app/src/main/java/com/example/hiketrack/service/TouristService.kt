package com.example.hiketrack.service

import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

data class TouristPoint(val name: String, val location: LatLng)

class TouristService {

    private val client = OkHttpClient()

    fun getTouristPoints(): List<TouristPoint> {
        val request = Request.Builder()
            .url("https://api.jsonbin.io/v3/b/673d83d4e41b4d34e4574ab8")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonObject = JSONObject(response.body?.string())
            val jsonArray = jsonObject.getJSONArray("record")
            val points = mutableListOf<TouristPoint>()

            for (i in 0 until jsonArray.length()) {
                val jsonPoint = jsonArray.getJSONObject(i)
                val name = jsonPoint.getString("name")
                val latitude = jsonPoint.getDouble("latitude")
                val longitude = jsonPoint.getDouble("longitude")
                points.add(TouristPoint(name, LatLng(latitude, longitude)))
            }

            return points
        }
    }
}