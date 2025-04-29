package com.example.brokenomore_prog7313

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityLoginBinding
import com.example.brokenomore_prog7313.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.registerbtn.setOnClickListener{
            val email = binding.rgtEmail.text.toString()
            val password = binding.rgtPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                registerUser(email, password)
                Toast.makeText(this, "User successfully registered",
                    Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "please ensure that all fields are filled in correctly",
                    Toast.LENGTH_LONG).show()
            }
        }

        binding.goBackToLoginTxt.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(email: String, password: String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "User was created successfully")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "Failed to create user", task.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}