package com.example.hiketrack

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hiketrack.model.Publicacion

class PublicacionAdapter(private val publicaciones: List<Publicacion>) :
    RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>() {

    inner class PublicacionViewHolder(itemView:
                                      View) : RecyclerView.ViewHolder(itemView),
        Parcelable {
        constructor(parcel: Parcel) : this(TODO("itemView")) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<PublicacionViewHolder> {
            override fun createFromParcel(parcel: Parcel): PublicacionViewHolder {
                return PublicacionViewHolder(parcel)
            }

            override fun newArray(size: Int): Array<PublicacionViewHolder?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)
        return PublicacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val publicacion = publicaciones[position]

        holder.itemView.apply {
            user_name.text = publicacion.usuario?.nombre ?: "Anónimo"
            post_date.text = publicacion.fecha.toString()
            post_description.text = publicacion.descripcion

            // Cargar imagen de perfil
            Glide.with(context)
                .load(publicacion.usuario?.imagenPerfil)
                .placeholder(R.drawable.perfil)
                .into(profile_image)

            // Cargar imagen de la publicación
            if (!publicacion.imagenUrl.isNullOrEmpty()) {
                Glide.with(context)
                    .load(publicacion.imagenUrl)
                    .into(post_image)
            }
        }
    }

    override fun getItemCount() = publicaciones.size
}