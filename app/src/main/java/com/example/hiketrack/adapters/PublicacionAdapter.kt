package com.example.hiketrack.adapters

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiketrack.model.Publicacion
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ItemPublicacionBinding
import com.google.firebase.storage.FirebaseStorage


class PublicacionAdapter(private val publicaciones: List<Publicacion>) :
    RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>() {

    inner class PublicacionViewHolder(val binding: ItemPublicacionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val binding = ItemPublicacionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PublicacionViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val publicacion = publicaciones[position]

        with(holder.binding) {
            // Asignar datos básicos de la publicación
            userName.text = publicacion.usuario?.nombre ?: "Anónimo"
            postDate.text = publicacion.fecha.toString()
            postDescription.text = publicacion.descripcion

            // Cargar imagen de perfil desde Firebase Storage
            publicacion.usuario?.let { usuario ->
                val profileImageRef = FirebaseStorage.getInstance().reference.child("images/${usuario.usuario}.jpg")
                profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(root.context)
                        .load(uri)
                        .placeholder(R.drawable.perfil) // Imagen predeterminada si falla la carga
                        .into(profileImage)
                }.addOnFailureListener {
                    profileImage.setImageResource(R.drawable.perfil) // Imagen por defecto si no se encuentra la imagen
                }
            }

            // Cargar imagen de la publicación desde Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("images/${publicacion.id}.jpg")

            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(root.context)
                    .load(uri) // Usar la URL de descarga generada
                    .placeholder(R.drawable.legviewhikerforest) // Imagen predeterminada mientras carga
                    .into(postImage)
            }.addOnFailureListener {
                postImage.setImageResource(R.drawable.legviewhikerforest) // Imagen por defecto si falla
                Log.e("PublicacionAdapter", "Error al cargar imagen: ${it.message}")
            }

        }
    }


    override fun getItemCount() = publicaciones.size
}