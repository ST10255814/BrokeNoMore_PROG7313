package com.example.brokenomore_prog7313

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.brokenomore_prog7313.databinding.ActivityHomeScreenBinding
import com.example.brokenomore_prog7313.databinding.ActivityTransactionsBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var categoryExpenseDataList: ArrayList<CategoryExpensesDataClass>
    private lateinit var transactionExpensesDataList: ArrayList<TransactionExpenseDataClass>
    lateinit var imageList: Array<Int>
    lateinit var dateList: Array<String>
    lateinit var categoryList: Array<String>
    lateinit var transactionList: Array<String>
    lateinit var amountList: Array<String>
    lateinit var transactionAmountList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        imageList = arrayOf(
            R.drawable.ic_list,
            R.drawable.ic_checkbox,
            R.drawable.ic_image,
            R.drawable.ic_toggle,
            R.drawable.ic_date,
            R.drawable.ic_rating,
            R.drawable.ic_time,
            R.drawable.ic_text,
            R.drawable.ic_edit,
            R.drawable.ic_camera)

        categoryList = arrayOf(
            "Food",
            "Utilities",
            "Travel",
            "Shopping",
            "Rent",
            "Going Out",
            "Car",
            "Savings",
            "Activities",
            "Wedding")

        amountList = arrayOf(
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87"
        )

        dateList = Array(10) { "" }

        generateDateList()

        transactionList = arrayOf(
            "Food",
            "Utilities",
            "Travel",
            "Shopping",
            "Rent",
            "Going Out",
            "Car",
            "Savings",
            "Activities",
            "Wedding")

        transactionAmountList = arrayOf(
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87",
            "R2003.00",
            "R2120.87"
        )

        binding.budgetView.layoutManager = LinearLayoutManager(this)
        binding.budgetView.setHasFixedSize(true)

        binding.transactionView.layoutManager = LinearLayoutManager(this)
        binding.transactionView.setHasFixedSize(true)

        categoryExpenseDataList = arrayListOf<CategoryExpensesDataClass>()
        transactionExpensesDataList = arrayListOf<TransactionExpenseDataClass>()

        val adapterCategory = CategoryExpensesAdapterClass(categoryExpenseDataList) { clickedItem ->
            val intent = Intent(this, AllBudgets::class.java)
            startActivity(intent)
        }

        val adapterTransaction = TransactionExpenseAdapterClass(transactionExpensesDataList) { clickedItem ->
            val intent = Intent(this, TransactionManagement::class.java)
            startActivity(intent)
        }
        binding.budgetView.adapter = adapterCategory
        binding.transactionView.adapter = adapterTransaction

        getCategoryExpensesData()
        getTransactionsExpensesData()

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

        binding.logOut.setOnClickListener{
            auth.signOut()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }
    }
    private fun getCategoryExpensesData(){
        for(i in imageList.indices){
            val dataClass = CategoryExpensesDataClass(imageList[i], categoryList[i], amountList[i])
            categoryExpenseDataList.add(dataClass)
        }
    }

    private fun getTransactionsExpensesData(){
        for(i in dateList.indices){
            val dataClass = TransactionExpenseDataClass(dateList[i], transactionList[i], transactionAmountList[i])
            transactionExpensesDataList.add(dataClass)
        }
    }

    private fun generateDateList() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM")  // Format: day and abbreviated month

        for (i in dateList.indices) {
            // Add i days to the current date
            calendar.add(Calendar.DAY_OF_MONTH, i)
            dateList[i] = dateFormat.format(calendar.time)  // Format the date and store as a string
        }
    }
}