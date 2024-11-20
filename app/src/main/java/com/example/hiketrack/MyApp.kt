package com.example.hiketrack

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.messaging.token.addOnCompleteListener {
            if(!it.isSuccessful) {
                println("El token no fue generado")
                return@addOnCompleteListener
            }

            val token = it.result
            println("El token es: $token")
        }
    }
}