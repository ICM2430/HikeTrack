package com.example.hiketrack

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class BottomMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.inicioButton).setOnClickListener {
            val intent =  Intent(requireContext(),FeedActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageView>(R.id.retosButton).setOnClickListener {
            val intent =  Intent(requireContext(),RetosActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageView>(R.id.recorridosButton).setOnClickListener {
            val intent =  Intent(requireContext(),RecorridosActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageView>(R.id.perfilButton).setOnClickListener {
            val intent =  Intent(requireContext(),PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}
