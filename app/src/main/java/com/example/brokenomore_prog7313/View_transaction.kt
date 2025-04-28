package com.example.brokenomore_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityCategoryCreationBinding
import com.example.brokenomore_prog7313.databinding.ActivityViewTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import android.graphics.BitmapFactory
import android.util.Base64

class View_transaction : AppCompatActivity() {
    private lateinit var binding: ActivityViewTransactionBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityViewTransactionBinding.inflate(layoutInflater)
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
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "User logged out successfully", Toast.LENGTH_LONG).show()
        }

        binding.goBack.setOnClickListener{
            val intent = Intent(this, TransactionManagement::class.java)
            startActivity(intent)
            finish()
        }

        val transaction = intent.getSerializableExtra("transactions") as? TransactionDisplayDataClass

        if(transaction != null){
            binding.descriptionTextView.text = transaction.transactionDescription
            binding.startDateTextView.text = transaction.transactionStartDate
            binding.endTransactionTextView.text = transaction.transactionEndDate
            binding.transactionAmountTextView.text = "R%.2f".format(transaction.transactionAmount)
            binding.categoryTextView.text = transaction.categoryName

            val receiptBase64String = transaction.transactionReceipt
            if (!receiptBase64String.isNullOrEmpty()) {
                try {
                    val decodedBytes = Base64.decode(receiptBase64String, Base64.DEFAULT)
                    val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                    binding.transactionReceiptImageView.setImageBitmap(decodedBitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "No receipt attached to show", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}