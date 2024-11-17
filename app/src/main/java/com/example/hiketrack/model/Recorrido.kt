package com.example.hiketrack.model

class Recorrido {
    var nombre: String = ""
    var elevacion: String = ""
    var calificacion: Int = 0
    var distancia: Float = 0.0f
    var tiempoEstimado: Int = 0 // Tiempo en minutos

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
}
