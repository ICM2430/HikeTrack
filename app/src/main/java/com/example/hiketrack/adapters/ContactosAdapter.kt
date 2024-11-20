package com.example.hiketrack.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ActivityContactsAdapterBinding
import com.example.hiketrack.model.Chat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ContactosAdapter(
    private val contactos: List<Chat>,
    private val currentUserId: String,
    private val onClick: (Chat) -> Unit
) : RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder>() {

    inner class ContactoViewHolder(val binding: ActivityContactsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val binding = ActivityContactsAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val chat = contactos[position]
        with(holder.binding) {
            // Obtener nombre del contacto
            val otherUserId = chat.usuarios.keys.firstOrNull { it != currentUserId }

            if (otherUserId != null) {
                // Consultar los datos del otro usuario en Firebase
                val database = FirebaseDatabase.getInstance().reference
                database.child("users").child(otherUserId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val otherUserName = snapshot.child("nombre").getValue(String::class.java)
                            nombreContacto.text = otherUserName ?: "Desconocido"

                            // Cargar imagen de perfil del otro usuario si existe
                            val profileImageRef = FirebaseStorage.getInstance().reference.child("images/${otherUserId}.jpg")

                            profileImageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    Glide.with(root.context)
                                        .load(uri)
                                        .placeholder(R.drawable.perfil)
                                        .error(R.drawable.perfil)
                                        .circleCrop()
                                        .into(imagenPerfil)
                                }
                                .addOnFailureListener { exception ->
                                    imagenPerfil.setImageResource(R.drawable.perfil)
                                    Log.e("ChatAdapter", "Error al cargar imagen de perfil: ${exception.message}")
                                }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("ContactosAdapter", "Error al cargar datos del usuario: ${error.message}")
                            nombreContacto.text = "Desconocido"
                        }
                    })
            } else {
                nombreContacto.text = "Desconocido"
            }

            // Mostrar último mensaje
            ultimoMensaje.text = chat.ultimoMensaje?.contenido ?: "Sin mensajes"

            // Cargar imagen de perfil del último remitente
            Glide.with(root.context)
                .load(chat.ultimoMensaje?.remitente?.imagenPerfilUrl ?: R.drawable.perfil)
                .circleCrop()
                .into(imagenPerfil)

            // Acción al hacer clic en un contacto
            root.setOnClickListener { onClick(chat) }
        }
    }



    override fun getItemCount() = contactos.size
}
