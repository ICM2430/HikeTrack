package com.example.hiketrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hiketrack.ContactsAdapter
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ActivityContactsAdapterBinding
import com.example.hiketrack.model.Chat

class ContactosAdapter(
    private val contactos: List<Chat>,
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
            // Setear datos
            nombreContacto.text = chat.nombre
            ultimoMensaje.text = chat.ultimoMensaje?.contenido ?: "Sin mensajes"

            // Cargar imagen de perfil
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
