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
import com.google.firebase.auth.FirebaseAuth

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var categoryExpenseDataList: ArrayList<CategoryExpensesDataClass>
    lateinit var imageList: Array<Int>
    lateinit var categoryList: Array<String>
    lateinit var amountList: Array<Double>

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
            "ListView",
            "CheckBox",
            "ImageView",
            "Toggle Switch",
            "Date Picker",
            "Rating Bar",
            "Time Picker",
            "TextView",
            "EditText",
            "Camera")

        amountList = arrayOf(
            2003.00,
            2120.87,
            2003.00,
            2120.87,
            2003.00,
            2120.87,
            2003.00,
            2120.87,
            2003.00,
            2120.87)

        binding.budgetView.layoutManager = LinearLayoutManager(this)
        binding.budgetView.setHasFixedSize(true)

        categoryExpenseDataList = arrayListOf<CategoryExpensesDataClass>()

        getCategoryExpensesData()

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
    private fun getCategoryExpensesData(){
        for(i in imageList.indices){
            val dataClass = CategoryExpensesDataClass(imageList[i], categoryList[i], amountList[i])
            categoryExpenseDataList.add(dataClass)
        }
        binding.budgetView.adapter = CategoryExpensesAdapterClass(categoryExpenseDataList)
    }
}