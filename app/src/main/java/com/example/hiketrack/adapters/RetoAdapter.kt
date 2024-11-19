package com.example.hiketrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ItemRetoBinding
import com.example.hiketrack.model.Reto
import com.example.hiketrack.model.Usuario
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RetoAdapter(private val retos: List<Reto>, private val usuarioActual: Usuario) :
    RecyclerView.Adapter<RetoAdapter.RetoViewHolder>() {

    inner class RetoViewHolder(val binding: ItemRetoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val binding = ItemRetoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RetoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        val reto = retos[position]

        with(holder.binding) {
            retoTitulo.text = reto.nombre
            retoDescripcion.text = reto.descripcion
            retoFecha.text = "Fecha límite: ${reto.fechaFin}"

            val storageRef = FirebaseStorage.getInstance().reference.child("retos/${reto.id}.jpg")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(root.context)
                    .load(uri)
                    .placeholder(R.drawable.pumpkin_ic)
                    .into(retoImage)
            }.addOnFailureListener {
                retoImage.setImageResource(R.drawable.pumpkin_ic)
            }

            if (reto.estaUsuarioUnido(usuarioActual.correo)) {
                botonUnirse.isSelected = true
                botonUnirse.text = "Unido"
            } else {
                botonUnirse.isSelected = false
                botonUnirse.text = "Unirse"
            }

            botonUnirse.setOnClickListener {
                if (reto.estaUsuarioUnido(usuarioActual.correo)) {
                    // Lógica para salir del reto
                    reto.participantes.removeIf { it.correo == usuarioActual.correo }
                    botonUnirse.isSelected = false
                    botonUnirse.text = "Unirse"
                    actualizarParticipantesEnFirebase(reto)
                } else {
                    // Lógica para unirse al reto
                    val usuarioActual = Usuario().apply { correo = usuarioActual.correo }
                    reto.participantes.add(usuarioActual)
                    botonUnirse.isSelected = true
                    botonUnirse.text = "Unido"
                    actualizarParticipantesEnFirebase(reto)
                }
            }
        }
    }

    private fun actualizarParticipantesEnFirebase(reto: Reto) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("retos").child(reto.id!!).child("participantes").setValue(reto.participantes)
    }

    override fun getItemCount() = retos.size
}
