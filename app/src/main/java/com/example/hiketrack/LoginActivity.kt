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


        binding.createAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java) // Define el intent para RegisterActivity
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(validateForm(email, password)){
                //API FIREBASE
                loginUser(email,password)

            }
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
                updateUI(auth.currentUser)
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
        } else if (!validEmailAddress(email)) {
            binding.usernameEditText.setError("Dirección de correo electrónico inválida")
        }else if (password.length < 5){
            binding.passwordEditText.setError("La contraseña debe tener al menos 5 caractéres")
        }else {
            valid = true
        }
        return valid
    }

    private fun validEmailAddress(email:String):Boolean{
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(regex.toRegex())
    }


}