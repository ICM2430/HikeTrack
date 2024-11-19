package com.example.hiketrack.model

import java.time.Duration

class Usuario {
    var nombre: String = ""
    var usuario: String = ""
    var contraseña: String = ""
    var correo: String = ""
    var seguidores: Int = 0
    var seguidos: Int = 0
    var estadisticas: Estadistica = Estadistica()
    var listaRetos: MutableList<Reto> = mutableListOf()
    var listaRecorridos: MutableList<Recorrido> = mutableListOf()

    constructor()
    constructor(nombre: String, usuario: String, correo: String) {
        this.nombre = nombre
        this.usuario = usuario
        this.correo = correo
    }

    fun buscarPerfil(nombreUsuario: String): Usuario? {
        // Implementar lógica para buscar un perfil
        return null
    }

    fun crearRecorrido(nombre: String): Recorrido {
        val recorrido = Recorrido()
        recorrido.nombre = nombre
        listaRecorridos.add(recorrido)
        return recorrido
    }

    fun reportarEmergencia() {
        println("Emergencia reportada.")
    }

    fun iniciarSesion() {
        println("Usuario iniciado sesión: $usuario")
    }
}
