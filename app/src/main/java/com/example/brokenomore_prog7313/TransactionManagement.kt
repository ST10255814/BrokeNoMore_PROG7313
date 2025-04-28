package com.example.brokenomore_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brokenomore_prog7313.databinding.ActivityTransactionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TransactionManagement : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var transactionDisplayList: ArrayList<TransactionDisplayDataClass>
    private lateinit var categoryArrayList: ArrayList<CategoryDatabaseData>
    private lateinit var transactionArrayList : ArrayList<TransactionsLogs>
    private lateinit var transactionRecyclerView : RecyclerView
    private lateinit var auth : FirebaseAuth
    private lateinit var transactionDatabaseRef: DatabaseReference
    private lateinit var firebaseCategoriesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
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

        binding.addTransactions.setOnClickListener{
            val intent = Intent(this, AddTransaction::class.java)
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
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }

        transactionRecyclerView = binding.allTransactionsRecyclerView
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionRecyclerView.setHasFixedSize(true)

        transactionArrayList = arrayListOf<TransactionsLogs>()
        categoryArrayList = arrayListOf<CategoryDatabaseData>()
        transactionDisplayList = arrayListOf<TransactionDisplayDataClass>()

        combineCategoryAndTransactionDataForDisplay()
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
                Toast.makeText(this@TransactionManagement, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getTransactionData(onComplete: () -> Unit) {
        transactionDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Transactions")

        transactionDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionArrayList.clear()
                if(snapshot.exists()){
                    for(transactionSnapshot in snapshot.children){
                        val transaction = transactionSnapshot.getValue(TransactionsLogs::class.java)
                        transactionArrayList.add(transaction!!)
                    }
                }
                onComplete()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TransactionManagement, "Failed to load transactions: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun combineCategoryAndTransactionDataForDisplay() {
        transactionDisplayList.clear()

        getCategoryData {
            getTransactionData {
                for (transaction in transactionArrayList) {
                    val matchingCategory = categoryArrayList.find { it.categoryName == transaction.category }
                    val category64BaseIcon = matchingCategory?.categoryIcon ?: ""

                    val transactionWithIcon = TransactionDisplayDataClass(
                        transactionStartDate = transaction.startDate,
                        transactionEndDate = transaction.endDate,
                        categoryImage = category64BaseIcon,
                        categoryName = transaction.category,
                        transactionDescription = transaction.description,
                        transactionAmount = transaction.amount,
                        transactionReceipt = transaction.receipt
                    )
                    transactionDisplayList.add(transactionWithIcon)
                }

                transactionRecyclerView.adapter = TransactionLogAdapterClass(transactionDisplayList) { transaction ->
                    val intent = Intent(this@TransactionManagement, View_transaction::class.java)
                    intent.putExtra("transactions", transaction)
                    startActivity(intent)
                    finish()
                }
                displayTextInTransactionCard()
            }
        }
    }
    private fun displayTextInTransactionCard(){
        var totalExpense = 0.0

        for (transaction in transactionDisplayList) {
            totalExpense += transaction.transactionAmount
        }
        val totalMinText = "R%.2f".format(totalExpense)

        binding.totalExpensesTextDisplay.text = totalMinText
    }
}