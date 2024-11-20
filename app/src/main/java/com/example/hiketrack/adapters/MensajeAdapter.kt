package com.example.hiketrack.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiketrack.databinding.ItemMensajeBinding
import com.example.hiketrack.model.Mensaje
import com.example.hiketrack.model.Usuario

class MensajeAdapter(
    private var mensajes: List<Mensaje>,
    private val currentUser: Usuario // Pass the current user to determine alignment
) : RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {


    inner class MensajeViewHolder(val binding: ItemMensajeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val binding = ItemMensajeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        with(holder.binding) {

            contenidoMensaje.text = mensaje.contenido
            fechaMensaje.text = mensaje.fechaEnvio

            if (mensaje.remitente?.usuario == currentUser.usuario) {
                // Message from the current user - align to the right
                contenidoMensaje.gravity = Gravity.END
                fechaMensaje.gravity = Gravity.END
            } else {
                // Message from another user - align to the left
                contenidoMensaje.gravity = Gravity.START
                fechaMensaje.gravity = Gravity.START
            }
        }
    }

    override fun getItemCount() = mensajes.size
}
