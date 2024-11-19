package com.example.hiketrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ItemRetoBinding
import com.example.hiketrack.model.Reto
import com.google.firebase.storage.FirebaseStorage

class RetoAdapter(private val retos: List<Reto>) :
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

            unirseButton.setOnClickListener {
                // Lógica para unirse al reto
            }
        }
    }

    override fun getItemCount() = retos.size
}
