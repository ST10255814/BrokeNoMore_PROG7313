package com.example.brokenomore_prog7313

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityBudgetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BudgetManagement : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseCategoriesRef: DatabaseReference
    private lateinit var firebaseBudgetRef: DatabaseReference
    val categoryList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        binding = ActivityBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBudgetButton.setOnClickListener {
            val intent = Intent(this, BudgetManagement::class.java)
            startActivity(intent)
            finish()
        }

        binding.transactionHistory.setOnClickListener{
            val intent = Intent(this, TransactionManagement::class.java)
            startActivity(intent)
            finish()
        }

        binding.home.setOnClickListener{
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }

        binding.gamificationTab.setOnClickListener {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_LONG).show()
        }

        binding.walletTab.setOnClickListener {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_LONG).show()
        }

        binding.logout.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }

        binding.createCategoryNavBtn.setOnClickListener {
            val intent = Intent(this, CategoryCreation::class.java)
            startActivity(intent)
            finish()
        }

        fetchCategoriesFromFB()

        binding.createBudgetBtn.setOnClickListener {
            sendBudgetData()
        }
    }
    private fun sendBudgetData(){
        firebaseBudgetRef = FirebaseDatabase.getInstance().getReference().child("Budgets")

        val budgetName = binding.budgetNameTxt.text.toString()
        val minAmount = binding.minAmountTxt.text.toString().toDouble()
        val maxAmount = binding.maxAmountTxt.text.toString().toDouble()
        val category = binding.categorySpinner.selectedItem.toString()

        if(budgetName.isNotEmpty() && minAmount.toString().isNotEmpty() && maxAmount.toString().isNotEmpty() && category.isNotEmpty()){
            val dataId = firebaseBudgetRef.push().key?:return
            val id = BudgetInsertData(dataId, budgetName, minAmount, maxAmount, category)

            firebaseBudgetRef.child(dataId).setValue(id).addOnSuccessListener {
                Toast.makeText(this,"Budget created successfully",Toast.LENGTH_LONG).show()
                binding.budgetNameTxt.text.clear()
                binding.minAmountTxt.text.clear()
                binding.maxAmountTxt.text.clear()
                binding.categorySpinner.setSelection(0)
            }
        }else{
            Toast.makeText(this, "Please enter correct information", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateSpinner(categories: List<String>){
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    private fun fetchCategoriesFromFB() {
        firebaseCategoriesRef = FirebaseDatabase.getInstance().getReference().child("AllCategories")

        firebaseCategoriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
}