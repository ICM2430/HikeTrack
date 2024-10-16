package com.example.hiketrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityLoginBinding
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    val TAG = "FIREBASE_APP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        val createAccountText = SpannableString("Crear cuenta")
        createAccountText.setSpan(UnderlineSpan(), 0, createAccountText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.createAccountTextView.text = createAccountText

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java) // Define el intent para FeedActivity
            startActivity(intent)
        }


        binding.createAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java) // Define el intent para RegisterActivity
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            //Already signed in
            val i = Intent(this, FeedActivity::class.java)
            i.putExtra("email", currentUser.email.toString())
            startActivity(i)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                updateUI(auth.currentUser) //nunca nulo si la autenticación es exitosa
            }else{
                val message = it.exception?.message
                Toast.makeText(baseContext, message, Toast.LENGTH_SHORT)
                Log.e(TAG, "Inicio de sesión fallido: $message")
                binding.usernameEditText.text.clear()
                binding.passwordEditText.text.clear()
            }
        }
    }

    private fun validateForm(email : String, password: String) : Boolean {
        var valid = false
        if (email.isEmpty()) {
            binding.usernameEditText.setError("Campo requerido")
        } else if (password.isEmpty()) {
            binding.passwordEditText.setError("Campo requerido")
        } else if (password.length < 5){
            binding.passwordEditText.setError("La contraseña debe tener al menos 5 caractéres")
        }else {
            valid = true
        }
        return valid
    }


}