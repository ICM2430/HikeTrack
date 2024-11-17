package com.example.hiketrack.model

import org.json.JSONObject
import java.util.Date

class MyLocation {
    class MyLocation(
        val latitude: Double,
        val longitude: Double,
        val date : Date,
        val distanciaRecorrida: Int,
        val steps: Int,
        val elapsedTime: Long
    ){
        fun toJSON() : JSONObject {
            val obj = JSONObject();
            obj.put("latitude", latitude)
            obj.put("longitude", longitude)
            obj.put("date", date)
            obj.put("distanciaRecorrida", distanciaRecorrida)
            obj.put("steps", steps)
            obj.put("elapsedTime", elapsedTime)
            return obj
        }
    }
}