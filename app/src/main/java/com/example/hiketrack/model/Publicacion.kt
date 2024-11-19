package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Publicacion {
    var id: String? = null
    var userId: String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    var fecha: String = ""
    var descripcion: String = ""

    fun editarPublicacion(nuevaDescripcion: String) {
        descripcion = nuevaDescripcion
        println("Publicación editada: $descripcion")
    }

    fun eliminarPublicacion() {
        println("Publicación eliminada")
    }
}
