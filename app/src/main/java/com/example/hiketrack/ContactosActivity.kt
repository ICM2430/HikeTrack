package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiketrack.adapters.ContactosAdapter
import com.example.hiketrack.databinding.ActivityContactosBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.example.hiketrack.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactosActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContactosBinding
    private lateinit var database: DatabaseReference
    private val chats = mutableListOf<Chat>()
    private lateinit var adapter: ContactosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            adapter = ContactosAdapter(chats, currentUserId) { chat, otherUserId ->
                if (otherUserId != null) {
                    Log.d("ContactosActivity", "Chat seleccionado con usuario: $otherUserId")

                    // Acción al seleccionar un chat
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("chatId", chat.id)
                    intent.putExtra("otherUserId", otherUserId) // Pasar UID del otro usuario
                    startActivity(intent)
                } else {
                    Log.e("ContactosActivity", "Error: No se pudo identificar al otro usuario.")
                    Toast.makeText(this, "Error al abrir el chat.", Toast.LENGTH_SHORT).show()
                }
            }
            binding.contactosRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.contactosRecyclerView.adapter = adapter
        } else {
            Log.e("ContactosActivity", "Error: Usuario no autenticado.")
            Toast.makeText(this, "Error al cargar contactos.", Toast.LENGTH_SHORT).show()
        }

        cargarChats()


    }

    private fun cargarChats() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        database.child("chats").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chats.clear()
                for (chatSnapshot in snapshot.children) {
                    val chat = chatSnapshot.getValue(Chat::class.java)
                    if (chat != null && chat.usuarios.containsKey(userId)) {
                        chat.id = chatSnapshot.key!!
                        chats.add(chat)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al cargar chats: ${error.message}")
            }
        })
    }
}