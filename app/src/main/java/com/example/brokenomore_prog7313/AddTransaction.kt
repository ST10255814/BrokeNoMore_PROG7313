package com.example.brokenomore_prog7313

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.SurfaceControl.Transaction
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransaction : AppCompatActivity() {
    private lateinit var binding : ActivityAddTransactionBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var categoryFirebaseRef: DatabaseReference
    private lateinit var transactionFirebaseRef: DatabaseReference
    val categoryList = mutableListOf<String>()
    var fileUri: Uri?= null
    var receiptBase64: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
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

        categoryFirebaseRef = FirebaseDatabase.getInstance().getReference().child("AllCategories")

        categoryFirebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
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

        binding.UploadReceipt.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose icon for category"), 0
            )
        }

        binding.submitTransaction.setOnClickListener {
            sendTransactionData()
        }
    }
    private fun updateSpinner(categories: List<String>){
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryTransactionSpinner.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null){
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                binding.receiptImage.setImageBitmap(bitmap)
                receiptBase64 = bitmapToBase64(bitmap)
            }catch (e: Exception){
                Log.e("Exception", "Error: " + e)
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun sendTransactionData() {
        transactionFirebaseRef = FirebaseDatabase.getInstance().getReference().child("Transactions")

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val description = binding.transactionDescriptionEditTxt.text.toString()
        val startDateInput = binding.transactionStartDateInput.text.toString()
        val endDateInput = binding.transactionEndDateInput.text.toString()

        val startDateObj: Date = formatter.parse(startDateInput)
        val endDateObj: Date = formatter.parse(endDateInput)

        val formattedStartDate = formatter.format(startDateObj)
        val formattedEndDate = formatter.format(endDateObj)

        val amount = binding.transactionAmountEditTxt.text.toString().toDouble()
        val category = binding.categoryTransactionSpinner.selectedItem.toString()

        // 💡 If no receipt uploaded, assign default text
        val finalReceipt = if (receiptBase64.isEmpty()) {
            "No receipt attached"
        } else {
            receiptBase64
        }

        if (category.isNotEmpty() && description.isNotEmpty() && startDateInput.isNotEmpty() && endDateInput.isNotEmpty()) {
            val dataId = transactionFirebaseRef.push().key ?: return
            val id = TransactionInsertDataClass(
                dataId,
                description,
                formattedStartDate,
                formattedEndDate,
                category,
                amount,
                finalReceipt
            )
            transactionFirebaseRef.child(dataId).setValue(id).addOnSuccessListener {
                Toast.makeText(this, "Transaction logged successfully", Toast.LENGTH_LONG).show()
                binding.transactionDescriptionEditTxt.text.clear()
                binding.transactionEndDateInput.text.clear()
                binding.transactionStartDateInput.text.clear()
                binding.transactionAmountEditTxt.text.clear()
                binding.categoryTransactionSpinner.setSelection(0)
                binding.receiptImage.setImageDrawable(null)
                receiptBase64 = "" // Reset after successful upload
            }
        } else {
            Toast.makeText(this, "Please enter correct information", Toast.LENGTH_LONG).show()
        }
    }
}