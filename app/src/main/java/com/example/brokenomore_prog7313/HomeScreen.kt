package com.example.brokenomore_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.brokenomore_prog7313.databinding.ActivityHomeScreenBinding
import com.google.firebase.auth.FirebaseAuth

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.addBudgetButton.setOnClickListener {
            val intent = Intent(this, BudgetManagement::class.java)
            startActivity(intent)
        }

        binding.logOut.setOnClickListener{
            auth.signOut()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }



    }
}