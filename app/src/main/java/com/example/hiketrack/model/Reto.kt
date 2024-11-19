package com.example.hiketrack.model

import java.time.LocalDateTime

class Reto(
    var nombre: String = "",
    var descripcion: String = "",
    var fechaInicio: String = "",
    var fechaFin: String = "",
    var participantes: MutableList<Usuario> = mutableListOf(),
    var imageUrl: String = ""
) {
    // Métodos adicionales
    fun iniciarReto() {
        println("Reto iniciado: $nombre")
    }

    fun crearReto(nombre: String, descripcion: String): Reto {
        val reto = Reto(nombre, descripcion)
        return reto
    }

    fun eliminarReto() {
        println("Reto eliminado: $nombre")
    }
}
