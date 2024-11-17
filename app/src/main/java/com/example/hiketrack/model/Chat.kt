package com.example.hiketrack.model

import java.time.LocalDateTime

class Chat {
    var nombre: String = ""
    var mensajes: MutableList<Mensaje> = mutableListOf()
    var creacion: LocalDateTime = LocalDateTime.now()

    fun enviarMensaje(mensaje: Mensaje) {
        mensajes.add(mensaje)
        println("Mensaje enviado: ${mensaje.contenido}")
    }

    fun eliminarMensaje(mensaje: Mensaje) {
        mensajes.remove(mensaje)
        println("Mensaje eliminado: ${mensaje.contenido}")
    }

    fun subirImagen(uri: String) {
        println("Imagen subida: $uri")
    }

    fun eliminarChat() {
        println("Chat eliminado")
    }
}
