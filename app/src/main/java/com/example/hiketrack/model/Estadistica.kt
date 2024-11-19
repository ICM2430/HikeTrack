package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration

class Estadistica {
    var distanciaRecorrida: Float = 0.0f
    var tiempoExplorandoEnMilisegundos: Long = 0L //
    var recorridosCompletados: Int = 0
    var pasosTotales: Int = 0

    fun tiempoExplorandoEnHoras() {
        //return tiempoExplorando.toHours()
    }

}