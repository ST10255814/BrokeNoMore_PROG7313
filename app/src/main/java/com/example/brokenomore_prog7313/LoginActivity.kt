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

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)


        binding.register.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBtn.setOnClickListener(){
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email, password)
            }else{
                Toast.makeText(this, "please ensure that all fields are filled in correctly",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "User signed in successfully")
                    val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "Failed to log in", task.exception)
                    Toast.makeText(
                        this,
                        "SignIn failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}