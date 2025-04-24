package com.example.brokenomore_prog7313

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brokenomore_prog7313.databinding.ActivityTransactionsBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class TransactionManagement : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var transactionLogExpensesDataList: ArrayList<TransactionLogDataClass>
    lateinit var categoryImageList: Array<Int>
    lateinit var dateLogList: Array<String>
    lateinit var amountLogList: Array<String>
    lateinit var transactionDescriptionLogList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryImageList = arrayOf(
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

        transactionDescriptionLogList = arrayOf(
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

        amountLogList = arrayOf(
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

        dateLogList = Array(10) { "" }

        generateDateList()

        binding.allTransactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allTransactionsRecyclerView.setHasFixedSize(true)

        getTransactionsLogExpensesData()
    }

    private fun getTransactionsLogExpensesData(){
        for(i in dateLogList.indices){
            val dataClass = TransactionLogDataClass(dateLogList[i], categoryImageList[i], transactionDescriptionLogList[i], amountLogList[i])
            transactionLogExpensesDataList.add(dataClass)
        }
        binding.allTransactionsRecyclerView.adapter = TransactionLogAdapterClass(transactionLogExpensesDataList)
    }

    private fun generateDateList() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM")  // Format: day and abbreviated month

        for (i in dateLogList.indices) {
            // Add i days to the current date
            calendar.add(Calendar.DAY_OF_MONTH, i)
            dateLogList[i] = dateFormat.format(calendar.time)  // Format the date and store as a string
        }
    }
}