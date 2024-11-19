package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Reto {
    var nombre: String = ""
    var descripcion: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    var fechaInicio: LocalDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    var fechaFin: LocalDateTime = LocalDateTime.now()
    var participantes: MutableList<Usuario> = mutableListOf()

    fun iniciarReto() {
        println("Reto iniciado: $nombre")
    }

    fun crearReto(nombre: String, descripcion: String): Reto {
        val reto = Reto()
        reto.nombre = nombre
        reto.descripcion = descripcion
        return reto
    }

    fun eliminarReto() {
        println("Reto eliminado: $nombre")
    }
}
