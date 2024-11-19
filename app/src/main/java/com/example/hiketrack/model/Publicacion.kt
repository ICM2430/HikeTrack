package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Publicacion {
    var id: String? = null
    var usuario: Usuario? = null
    @RequiresApi(Build.VERSION_CODES.O)
    var fecha: LocalDateTime = LocalDateTime.now()
    var descripcion: String = ""
    var ubicacion: MyLocation? = null

    fun añadirPublicacion() {
        println("Publicación añadida por ${usuario?.nombre}")
    }

    fun editarPublicacion(nuevaDescripcion: String) {
        descripcion = nuevaDescripcion
        println("Publicación editada: $descripcion")
    }

    fun eliminarPublicacion() {
        println("Publicación eliminada")
    }
}
