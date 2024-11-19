package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration

class Estadistica {
    var distanciaRecorrida: Float = 0.0f
    @RequiresApi(Build.VERSION_CODES.O)
    var tiempoExplorando: Duration = Duration.ZERO
    var recorridosCompletados: Int = 0
    var pasosTotales: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    fun tiempoExplorandoEnHoras(): Long {
        return tiempoExplorando.toHours()
    }
}