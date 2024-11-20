package com.example.hiketrack.model

import java.time.LocalDateTime

class Mensaje {

    var remitente: Usuario? = null
    var receptor: Usuario? = null
    var contenido: String = ""
    var fechaEnvio: String = ""

    constructor()

    constructor(contenido: String, fechaEnvio: String, remitente: Usuario?, receptor: Usuario?) {
        this.contenido = contenido
        this.fechaEnvio = fechaEnvio
        this.remitente = remitente
        this.receptor = receptor
    }
}
