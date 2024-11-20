    package com.example.hiketrack.model

    import android.os.Build
    import androidx.annotation.RequiresApi
    import java.time.LocalDateTime
    import java.time.format.DateTimeFormatter

    class Chat {
        var nombre: String = ""
        var mensajes: MutableList<Mensaje> = mutableListOf()
        var creacion: String =""
        var ultimoMensaje: Mensaje? = null

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

        @RequiresApi(Build.VERSION_CODES.O)
        fun obtenerFechaCreacionFormateada(): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return creacion.format(formatter)
        }
    }
