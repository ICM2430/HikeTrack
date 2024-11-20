package com.example.hiketrack.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Chat {
    var id: String = ""
    var usuarios: MutableMap<String, Boolean> = mutableMapOf()
    var mensajes: MutableList<Mensaje> = mutableListOf() // Cambia a lista
    var creacion: String = ""
    var ultimoMensaje: Mensaje? = null

    constructor()

    constructor(
        id: String,
        usuarios: MutableMap<String, Boolean>,
        mensajes: MutableList<Mensaje> = mutableListOf(),
        creacion: String,
        ultimoMensaje: Mensaje?
    ) {
        this.id = id
        this.usuarios = usuarios
        this.mensajes = mensajes
        this.creacion = creacion
        this.ultimoMensaje = ultimoMensaje
    }
}
