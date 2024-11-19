package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Reto(
    var id: String? = null,
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
/*
    fun estaUsuarioUnido(usuarioId: String): Boolean {
        return participantes.any { it.id == usuarioId }
    }
*/
    fun eliminarReto() {
        println("Reto eliminado: $nombre")
    }
}
