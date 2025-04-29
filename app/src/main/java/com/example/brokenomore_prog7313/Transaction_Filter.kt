package com.example.brokenomore_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brokenomore_prog7313.databinding.ActivityTransactionFilterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class Transaction_Filter : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionFilterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var transactionFilterRecyclerView : RecyclerView
    private lateinit var categoryFilterRecyclerView : RecyclerView
    private lateinit var firebaseTransactionRef: DatabaseReference
    private lateinit var transactionArrayList : ArrayList<TransactionDataClass>
    private lateinit var categoryArrayList: ArrayList<CategoryDatabaseData>
    private lateinit var categoryFilterDisplayList: ArrayList<CategoryFilterData>
    private lateinit var firebaseCategoriesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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

        binding.returnbtn.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }

        transactionFilterRecyclerView = binding.transactionPeriodView
        categoryFilterRecyclerView = binding.categoryPeroidView

        transactionFilterRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryFilterRecyclerView.layoutManager = LinearLayoutManager(this)

        transactionFilterRecyclerView.setHasFixedSize(true)
        categoryFilterRecyclerView.setHasFixedSize(true)

        transactionArrayList = arrayListOf<TransactionDataClass>()
        categoryArrayList = arrayListOf<CategoryDatabaseData>()
        categoryFilterDisplayList = arrayListOf<CategoryFilterData>()

        binding.filterBtn.setOnClickListener {
            prepareCategoryDisplayData()
        }
    }
    private fun getTransactionFilteredData(onComplete: () -> Unit) {
        val filteredStartDate = binding.transactionStartDateFilter.text.toString()
        val filteredEndDate = binding.transactionEndDateFilter.text.toString()

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val startDateMillis = dateFormat.parse(filteredStartDate)?.time ?: 0L
        val endDateMillis = dateFormat.parse(filteredEndDate)?.time ?: 0L

        firebaseTransactionRef = FirebaseDatabase.getInstance().getReference().child("Transactions")

        firebaseTransactionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionArrayList.clear()
                if(snapshot.exists()){
                    for(transactionSnapshot in snapshot.children){
                        val transaction = transactionSnapshot.getValue(TransactionDataClass::class.java)
                        val transactionDate = transaction?.startDate

                        if(transaction != null && transactionDate != null){
                            try {
                                val transactionMillis = dateFormat.parse(transactionDate)?.time
                                if (transactionMillis != null &&
                                    transactionMillis in startDateMillis..endDateMillis
                                ) {
                                    transactionArrayList.add(transaction)
                                }
                            }catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                transactionFilterRecyclerView.adapter = TransactionFilterAdapterClass(transactionArrayList)
                onComplete()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Transaction_Filter, "Failed to load transactions: ${error.message}", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@Transaction_Filter, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun prepareCategoryDisplayData() {
        categoryFilterDisplayList.clear()

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val startDateMillis = dateFormat.parse(binding.transactionStartDateFilter.text.toString())?.time ?: 0L
        val endDateMillis = dateFormat.parse(binding.transactionEndDateFilter.text.toString())?.time ?: 0L

        getTransactionFilteredData {
            getCategoryData {
                val categoryTotalsMap = mutableMapOf<String, Double>()

                for (transaction in transactionArrayList) {
                    val transactionDate = transaction.startDate ?: continue
                    val transactionMillis = dateFormat.parse(transactionDate)?.time ?: continue

                    if (transactionMillis in startDateMillis..endDateMillis) {
                        val category = transaction.category ?: continue
                        val amount = transaction.amount ?: 0.0
                        categoryTotalsMap[category] = categoryTotalsMap.getOrDefault(category, 0.0) + amount
                    }
                }

                for ((categoryName, totalAmount) in categoryTotalsMap) {
                    val matchingCategory = categoryArrayList.find { it.categoryName == categoryName }
                    val icon = matchingCategory?.categoryIcon ?: ""

                    categoryFilterDisplayList.add(
                        CategoryFilterData(
                            categoryIcon = icon,
                            categoryName = categoryName,
                            amount = totalAmount
                        )
                    )
                }
                categoryFilterRecyclerView.adapter = CategoryFilterAdapterClass(categoryFilterDisplayList)
            }
        }
    }
}