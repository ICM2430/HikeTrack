package com.example.hiketrack.model

import java.time.LocalDateTime

class Publicacion {
    var usuario: Usuario? = null
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
