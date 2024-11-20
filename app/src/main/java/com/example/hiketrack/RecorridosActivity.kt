package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.hiketrack.databinding.ActivityRecorridosBinding
import com.example.hiketrack.fragments.BottomMenuFragment
import com.example.hiketrack.model.Recorrido
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage


class RecorridosActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecorridosBinding
    private lateinit var database: FirebaseDatabase
    private val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var myRef: DatabaseReference

    val recorridosRef = "recorridos/"




    lateinit var vel : ValueEventListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecorridosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.bottomMenuContainer.id, BottomMenuFragment())
        fragmentTransaction.commit()




        binding.settingsBtn.setOnClickListener {
            val intent = Intent (this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }


        binding.nuevorecorrido.setOnClickListener {
            val intent = Intent(this, MapsTrackerActivity::class.java)
            startActivity(intent)
        }




        val composeView = binding.composeView
        composeView.setContent {
            val recorridos = remember { mutableStateListOf<Recorrido>() }
            importarRecorridos(recorridos)
            showRecorridos(recorridos)
        }







    }

    fun importarRecorridos(recorridos: MutableList<Recorrido>) {
        // Esta funcion importa los recorridos de la base de datos y los guarda en la lista recorridos

        myRef = database.getReference(recorridosRef)
        var vel = myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recorridos.clear()
                for(child in snapshot.children){
                    val recorrido = child.getValue<Recorrido>()

                    if (recorrido != null) {
                        recorridos.add(recorrido)
                    }

                    if (recorrido != null) {
                        Log.i("Recorridos", recorrido.nombre)
                    }

                }

                // adapter.updateUsers(usersList)


            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Recorridos", "Error fetching data", error.toException())
            }
        })

    }


    override fun onPause() {
        myRef.removeEventListener(vel)
        super.onPause()
    }
    override fun onStart() {
        super.onStart()


    }
    
    @Composable
    fun showRecorridos(recorridos: List<Recorrido>) {

        LazyColumn{

            items(recorridos) { recorrido ->
                RecorridoItem(recorrido)

            }

        }


    }

    @Composable
    fun RecorridoItem(recorrido: Recorrido) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color(0xFF594E2C))

        ) {
            val (image, direccion, distancia, tiempo) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.imagen_ruta),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(100.dp)
            )

            Text(
                text = recorrido.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(direccion) {
                    start.linkTo(image.end, margin = 16.dp)
                    top.linkTo(parent.top, margin = 8.dp)

                }
            )


            Text(
                text = "${recorrido.distancia}km",
                color = Color.White,
                modifier = Modifier.constrainAs(distancia) {
                    start.linkTo(direccion.end, margin = 8.dp)
                    top.linkTo(direccion.top)
                    bottom.linkTo(parent.bottom)
                }
            )

            Text(
                text = "${recorrido.tiempoEstimado} min",
                color = Color.White,
                modifier = Modifier.constrainAs(tiempo) {
                    start.linkTo(distancia.end, margin = 8.dp)
                    top.linkTo(distancia.top)
                    bottom.linkTo(distancia.bottom)
                }
            )
        }
    }
}