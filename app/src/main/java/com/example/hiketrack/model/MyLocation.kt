package com.example.hiketrack.model

import org.json.JSONObject
import java.util.Date

class MyLocation(
    val latitude: Double,
    val longitude: Double,
    val date: Date
) {
    fun toJSON(): JSONObject {
        val obj = JSONObject()
        obj.put("latitude", latitude)
        obj.put("longitude", longitude)
        obj.put("date", date.time)
        return obj
    }
}
