package com.example.hiketrack.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import com.example.hiketrack.model.Publicacion
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.hiketrack.PerfilAjenoActivity
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ItemPublicacionBinding
import com.example.hiketrack.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class PublicacionAdapter(private val context: Context, private val publicaciones: List<Publicacion>) :
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
            postDate.text = publicacion.fecha.toString()
            postDescription.text = publicacion.descripcion


            //obtener usuario
            publicacion.userId?.let { userId ->
                val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usuario = snapshot.getValue(Usuario::class.java)
                        if (usuario != null) {
                            userName.text = usuario.usuario

                            // Cargar imagen de perfil desde Firebase Storage
                            val profileImageRef =
                                FirebaseStorage.getInstance().reference.child("images/${userId}.jpg")
                            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                                Glide.with(root.context)
                                    .load(uri)
                                    .placeholder(R.drawable.perfil) // Imagen predeterminada si falla la carga
                                    .into(profileImage)
                            }.addOnFailureListener {
                                profileImage.setImageResource(R.drawable.perfil) // Imagen por defecto si no se encuentra la imagen
                                Log.e("PublicacionAdapter", "Error al cargar imagen de perfil: ${it.message}")
                            }
                        } else {
                            userName.text = "Anónimo"
                            profileImage.setImageResource(R.drawable.perfil) // Imagen por defecto
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        userName.text = "Anónimo"
                        profileImage.setImageResource(R.drawable.perfil) // Imagen por defecto
                        Log.e("PublicacionAdapter", "Error al cargar datos del usuario: ${error.message}")
                    }
                })
            }

            // Cargar imagen de la publicación desde Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("publicaciones/${publicacion.id}.jpg")

            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(root.context)
                    .load(uri) // Usar la URL de descarga generada
                    .placeholder(R.drawable.legviewhikerforest) // Imagen predeterminada mientras carga
                    .into(postImage)
            }.addOnFailureListener {
                postImage.setImageResource(R.drawable.legviewhikerforest) // Imagen por defecto si falla
                Log.e("PublicacionAdapter", "Error al cargar imagen: ${it.message}")
            }

            profileImage.setOnClickListener {
                if (publicacion.userId != null) {
                    val intent = Intent(context, PerfilAjenoActivity::class.java)
                    intent.putExtra("userId", publicacion.userId)
                    context.startActivity(intent)
                } else {
                    Log.e("PublicacionAdapter", "El userId es nulo, no se puede abrir el perfil.")
                }
            }


        }
    }


    override fun getItemCount() = publicaciones.size
}