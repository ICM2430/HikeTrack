package com.example.hiketrack.model

import org.json.JSONArray
import org.json.JSONObject

class Recorrido {
    var nombre: String = ""
    var elevacion: String = ""
    var calificacion: Int = 0
    var distancia: Float = 0.0f
    var tiempoEstimado: Int = 0 // Tiempo en minutos
    var myLocations: MutableList<MyLocation> = mutableListOf()

    constructor(
        elevacion: String,
        distancia: Float,
        tiempoEstimado: Int,
        myLocations: MutableList<MyLocation>
    ) {
        this.elevacion = elevacion
        this.distancia = distancia
        this.tiempoEstimado = tiempoEstimado
        this.myLocations = myLocations
    }

    constructor()

    fun calificarRecorrido(puntuacion: Int) {
        calificacion = puntuacion
        println("Recorrido calificado con $puntuacion")
    }

    fun iniciarRecorrido() {
        println("Recorrido iniciado: $nombre")
    }

    fun crearRecorrido() {
        println("Recorrido creado: $nombre")
    }

    fun eliminarRecorrido() {
        println("Recorrido eliminado: $nombre")
    }

    fun finalizarRecorrido() {
        println("Recorrido finalizado: $nombre")
    }

    fun guardarRecorrido() {
        println("Recorrido guardado: $nombre")
    }


    // Funcion para convertir el recorrido a un JSON
    fun toJSON(): JSONObject {
        val obj = JSONObject()
        obj.put("nombre", nombre)
        obj.put("elevacion", elevacion)
        obj.put("calificacion", calificacion)
        obj.put("distancia", distancia)
        obj.put("tiempoEstimado", tiempoEstimado)

        val locationsArray = JSONArray()
        for (location in myLocations) {
            locationsArray.put(location.toJSON())
        }
        obj.put("myLocations", locationsArray)

        return obj
    }



}
