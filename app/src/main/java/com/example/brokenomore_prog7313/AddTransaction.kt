package com.example.brokenomore_prog7313

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.SurfaceControl.Transaction
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityAddTransactionBinding
import com.example.brokenomore_prog7313.databinding.ActivityHomeScreenBinding
import com.example.brokenomore_prog7313.databinding.ActivityTransactionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddTransaction : AppCompatActivity() {
    private lateinit var binding : ActivityAddTransactionBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseRef: DatabaseReference
    val categoryList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
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

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("AllCategories")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.child("categoryName").getValue(String::class.java)
                    if (categoryName != null) {
                        categoryList.add(categoryName)
                    }
                }
                updateSpinner(categoryList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to load categories.", error.toException())
            }
        })
    }
    private fun updateSpinner(categories: List<String>){
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryTransactionSpinner.adapter = adapter
    }
}