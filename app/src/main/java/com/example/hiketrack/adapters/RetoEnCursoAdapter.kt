package com.example.hiketrack.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hiketrack.R
import com.example.hiketrack.databinding.ItemRetoBinding
import com.example.hiketrack.databinding.ItemRetoEnCursoBinding
import com.example.hiketrack.model.Reto
import com.example.hiketrack.model.Usuario
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RetoEnCursoAdapter(private val retos: List<Reto>, private val usuarioActual: Usuario) :
    RecyclerView.Adapter<RetoEnCursoAdapter.RetoEnCursoViewHolder>() {

    inner class RetoEnCursoViewHolder(val binding: ItemRetoEnCursoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoEnCursoViewHolder {
        // Inflar el diseño `item_reto_en_curso.xml`
        val binding = ItemRetoEnCursoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RetoEnCursoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RetoEnCursoViewHolder, position: Int) {
        val reto = retos[position]

        with(holder.binding) {
            // Configurar el título del reto
            retoTitulo.text = reto.nombre

            // Mostrar el progreso calculado del reto (por ejemplo, basado en datos del usuario)
            val progreso = calcularProgreso(reto, usuarioActual)
            retoProgreso.text = reto.descripcion
            //retoProgreso.text = "Progreso: $progreso%"

            // Cargar la imagen del reto desde Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("retos/${reto.id}.jpg")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(root.context)
                    .load(uri)
                    .placeholder(R.drawable.pumpkin_ic) // Imagen predeterminada mientras carga
                    .into(retoImage)
            }.addOnFailureListener {
                retoImage.setImageResource(R.drawable.pumpkin_ic) // Imagen predeterminada si falla
                Log.e("RETOS", "No se encontró imagen")
            }
        }
    }

    private fun calcularProgreso(reto: Reto, usuario: Usuario): Int {
        //return (reto.participantes.find { it.correo == usuario.correo }?.progreso ?: 0)
        return 0
    }
    override fun getItemCount() = retos.size
}