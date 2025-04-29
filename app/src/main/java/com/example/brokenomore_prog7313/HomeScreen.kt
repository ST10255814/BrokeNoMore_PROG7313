package com.example.brokenomore_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.brokenomore_prog7313.databinding.ActivityHomeScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseTransactionRef: DatabaseReference
    private lateinit var firebaseBudgetRef: DatabaseReference
    private lateinit var firebaseCategoriesRef: DatabaseReference
    private lateinit var transactionRecyclerView : RecyclerView
    private lateinit var budgetRecylerView : RecyclerView
    private lateinit var transactionArrayList : ArrayList<TransactionDataClass>
    private lateinit var categoryArrayList: ArrayList<CategoryDatabaseData>
    private lateinit var budgetArrayList : ArrayList<BudgetDataClass>
    private lateinit var budgetDisplayArrayList : ArrayList<BudgetDisplayData>
    private var totalExpenseCost: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
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

        binding.filterTab.setOnClickListener {
            val intent = Intent(this, Transaction_Filter::class.java)
            startActivity(intent)
            finish()
        }

        binding.logOut.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }

        transactionRecyclerView = binding.transactionView
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionRecyclerView.setHasFixedSize(true)

        transactionArrayList = arrayListOf<TransactionDataClass>()
        getTransactionData()

        budgetRecylerView = binding.budgetView
        budgetRecylerView.layoutManager = LinearLayoutManager(this)
        budgetRecylerView.setHasFixedSize(true)

        budgetDisplayArrayList = arrayListOf<BudgetDisplayData>()
        categoryArrayList = arrayListOf<CategoryDatabaseData>()
        budgetArrayList = arrayListOf<BudgetDataClass>()
        combineBudgetAndCategoryDataForDisplay()
    }

    private fun getTransactionData() {
        firebaseTransactionRef = FirebaseDatabase.getInstance().getReference().child("Transactions")

        firebaseTransactionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionArrayList.clear()
                totalExpenseCost = 0.0
                if(snapshot.exists()){
                    for(transactionSnapshot in snapshot.children){
                        val transaction = transactionSnapshot.getValue(TransactionDataClass::class.java)
                        transactionArrayList.add(transaction!!)
                        totalExpenseCost += transaction.amount ?: 0.0
                    }
                    transactionRecyclerView.adapter = TransactionAdapterClass(transactionArrayList) { transaction ->
                        val intent = Intent(this@HomeScreen, TransactionManagement::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeScreen, "Failed to load transactions: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getBudgetData(onComplete: () -> Unit){
        firebaseBudgetRef = FirebaseDatabase.getInstance().getReference().child("Budgets")

        firebaseBudgetRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                budgetArrayList.clear()
                if(snapshot.exists()){
                    for(budgetSnapshot in snapshot.children){
                        val budget = budgetSnapshot.getValue(BudgetDataClass::class.java)
                        budgetArrayList.add(budget!!)
                    }
                }
                onComplete()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeScreen, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getCategoryData(onComplete: () -> Unit){
        firebaseCategoriesRef = FirebaseDatabase.getInstance().getReference().child("AllCategories")

        firebaseCategoriesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                if(snapshot.exists()){
                    for(categorySnapshot in snapshot.children){
                        val category = categorySnapshot.getValue(CategoryDatabaseData::class.java)
                        categoryArrayList.add(category!!)
                    }
                }
                onComplete()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeScreen, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun combineBudgetAndCategoryDataForDisplay() {
        budgetDisplayArrayList.clear()

        getCategoryData {
            getBudgetData {
                for (budget in budgetArrayList) {
                    val matchingCategory = categoryArrayList.find { it.categoryName == budget.category }
                    val category64BaseIcon = matchingCategory?.categoryIcon ?: ""

                    val budgetWithIcon = BudgetDisplayData(
                        categoryImage = category64BaseIcon,
                        budgetName = budget.budgetName,
                        minAmount = budget.minAmount,
                        maxAmount = budget.maxAmount
                    )
                    budgetDisplayArrayList.add(budgetWithIcon)
                }

                budgetRecylerView.adapter = BudgetAdapterClass(budgetDisplayArrayList) { transaction ->
                    val intent = Intent(this@HomeScreen, TransactionManagement::class.java)
                    startActivity(intent)
                    finish()
                }

                totalMinAndMaxBudgetAmount()
            }
        }
    }

    private fun totalMinAndMaxBudgetAmount(){
        var totalMin = 0.0
        var totalMax = 0.0

        for (budget in budgetDisplayArrayList) {
            totalMin += budget.minAmount
            totalMax += budget.maxAmount
        }
        val finalAmountLeft = (totalMax - totalMin) - totalExpenseCost

        val totalMaxText = "out of R%.2f budgeted".format(totalMax)
        val totalMinText = "R%.2f left".format(finalAmountLeft)

        binding.maxAmountTextDisplay.text = totalMaxText
        binding.minAmountTextDisplay.text = totalMinText
    }
}