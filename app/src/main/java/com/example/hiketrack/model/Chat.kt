    package com.example.hiketrack.model

    import android.os.Build
    import androidx.annotation.RequiresApi
    import java.time.LocalDateTime
    import java.time.format.DateTimeFormatter

    class Chat {
        var id: String = ""
        var usuarios: MutableMap<String, Boolean> = mutableMapOf()
        var mensajes: MutableList<Mensaje> = mutableListOf()
        var creacion: String = ""
        var ultimoMensaje: Mensaje? = null

        constructor()

        constructor(
            id: String,
            usuarios: MutableMap<String, Boolean>,
            mensajes: MutableList<Mensaje>,
            creacion: String,
            ultimoMensaje: Mensaje?
        ) {
            this.id = id
            this.usuarios = usuarios
            this.mensajes = mensajes
            this.creacion = creacion
            this.ultimoMensaje = ultimoMensaje
        }

        fun enviarMensaje(mensaje: Mensaje) {
            mensajes.add(mensaje) // Agrega el mensaje a la lista
            ultimoMensaje = mensaje // Actualiza el último mensaje
            println("Mensaje enviado: ${mensaje.contenido}")
        }

        fun eliminarMensaje(mensaje: Mensaje) {
            if (mensajes.remove(mensaje)) { // Remueve el mensaje si existe
                println("Mensaje eliminado: ${mensaje.contenido}")
                // Actualiza último mensaje al más reciente
                ultimoMensaje = mensajes.lastOrNull()
            } else {
                println("Mensaje no encontrado en el chat")
            }
        }

        fun subirImagen(uri: String) {
            println("Imagen subida: $uri")
        }

        fun eliminarChat() {
            mensajes.clear()
            ultimoMensaje = null
            println("Chat eliminado")
        }
    }
