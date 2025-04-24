package com.example.brokenomore_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityBudgetBinding
import com.google.firebase.auth.FirebaseAuth

class BudgetManagement : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        binding = ActivityBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBudgetButton.setOnClickListener {
            val intent = Intent(this, BudgetManagement::class.java)
            startActivity(intent)
        }

        binding.transactionHistory.setOnClickListener{
            val intent = Intent(this, TransactionManagement::class.java)
            startActivity(intent)
        }

        binding.home.setOnClickListener{
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener{
            auth.signOut()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }

        binding.createCategoryNavBtn.setOnClickListener {
            val intent = Intent(this, CategoryCreation::class.java)
            startActivity(intent)
        }


    }
}