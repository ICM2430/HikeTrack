package com.example.hiketrack.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hiketrack.PerfilAjenoActivity
import com.example.hiketrack.R
import com.example.hiketrack.model.Publicacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PublicacionNotificationService : Service() {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var publicacionesListener: ChildEventListener
    private val publicacionesMap = mutableMapOf<String, Publicacion>()

    override fun onCreate() {
        super.onCreate()
        Log.d("PublicacionService", "Servicio iniciado")
        createNotificationChannel()
        listenForNewPublicaciones()
    }

    private fun listenForNewPublicaciones() {
        publicacionesListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("PublicacionService", "onChildAdded triggered")

                val publicacion = snapshot.getValue(Publicacion::class.java)
                val userId = publicacion?.userId

                if (userId == null) {
                    Log.e("PublicacionService", "User ID is null, ignoring snapshot: $snapshot")
                    return
                }

                // Verificar que no sea la publicación del usuario actual
                val currentUser = auth.currentUser
                if (currentUser != null && userId == currentUser.uid) {
                    Log.d("PublicacionService", "Ignoring publication from current user: $userId")
                    return
                }

                // Verificar si ya fue procesada
                if (!publicacionesMap.containsKey(snapshot.key)) {
                    publicacionesMap[snapshot.key!!] = publicacion
                    Log.d("PublicacionService", "New publication detected from user: $userId")
                    sendNotification(publicacion)
                } else {
                    Log.d("PublicacionService", "Publication already processed: ${snapshot.key}")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("PublicacionService", "onChildChanged triggered, ignoring updates")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("PublicacionService", "onChildRemoved triggered, removing from map")
                publicacionesMap.remove(snapshot.key)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("PublicacionService", "onChildMoved triggered, ignoring movements")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PublicacionService", "Error listening for publications: ${error.message}")
            }
        }

        database.child("publicaciones").addChildEventListener(publicacionesListener)
    }

    private fun sendNotification(publicacion: Publicacion) {
        Log.d("PublicacionService", "Preparing notification for publication: ${publicacion.id}")

        val userId = publicacion.userId
        if (userId.isNullOrEmpty()) {
            Log.e("PublicacionService", "Cannot send notification: UserId is null or empty")
            return
        }

        val intent = Intent(this, PerfilAjenoActivity::class.java).apply {
            putExtra("userId", userId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "PUBLICACIONES_CHANNEL")
            .setContentTitle("Nueva publicación")
            .setContentText("¡Mira: ${publicacion.descripcion}")
            .setSmallIcon(R.drawable.bell)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(publicacion.userId.hashCode(), notification)

        Log.d("PublicacionService", "Notification sent for publication: ${publicacion.id}")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "PUBLICACIONES_CHANNEL",
                "Notificaciones de Publicaciones",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notificaciones cuando se publican nuevas publicaciones"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("PublicacionService", "Notification channel created")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        database.child("publicaciones").removeEventListener(publicacionesListener)
        Log.d("PublicacionService", "Service destroyed and listener removed")
    }
}
