package com.example.hiketrack.model

import android.os.Build
import android.util.Log
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

    fun estaUsuarioUnido(usuarioIdentificador: String): Boolean {
        return participantes.any {
            Log.e("RETOS", "UserCorreoEnConcidicional: ${it.correo}")
            it.correo == usuarioIdentificador
        }
    }




    fun eliminarReto() {
        println("Reto eliminado: $nombre")
    }
}
