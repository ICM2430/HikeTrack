package com.example.hiketrack.model

import java.time.Duration

class Estadistica {
    var distanciaRecorrida: Float = 0.0f
    var tiempoExplorando: Duration = Duration.ZERO
    var recorridosCompletados: Int = 0
    var pasosTotales: Int = 0

    fun tiempoExplorandoEnHoras(): Long {
        return tiempoExplorando.toHours()
    }
}