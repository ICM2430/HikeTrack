package com.example.hiketrack.model

import java.time.LocalDateTime

class Mensaje {

    var remitente: Usuario? = null
    var receptor: Usuario? = null
    var contenido: String = ""
    var fechaEnvio: String = ""
}
