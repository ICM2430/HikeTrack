package com.example.hiketrack.model

import java.time.Duration

class Usuario {
    var nombre: String = ""
    var usuario: String = ""
    var correo: String = ""
    var contactoEmergencia: String = "101"
    var imagenPerfilUrl: String? = null
    var estadisticas: Estadistica = Estadistica()
    var usuariosConversados: MutableList<Usuario> = mutableListOf()
    var listaRetos: MutableList<Reto> = mutableListOf()
    var listaRecorridos: MutableList<Recorrido> = mutableListOf()
    var latitude: Double? = null
    var longitude: Double? = null

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
