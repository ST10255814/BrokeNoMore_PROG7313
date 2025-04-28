package com.example.brokenomore_prog7313

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.brokenomore_prog7313.databinding.ActivityCategoryCreationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class CategoryCreation : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryCreationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var fileUri: Uri? = null
    var categoryIconBase64String: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityCategoryCreationBinding.inflate(layoutInflater)
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

        binding.categoryNameTxt.addTextChangedListener {
            binding.categoryNamePreviewTxt.text = it.toString()
        }

        binding.categoryIconUploadBtn.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose icon for category"), 0
            )
        }

        database = FirebaseDatabase.getInstance().reference.child("AllCategories")
        binding.createCategoryBtn.setOnClickListener {
            sendCategoryData(categoryIconBase64String)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null){
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                binding.categoryIconCustom.setImageBitmap(bitmap)
                categoryIconBase64String = bitmapToBase64(bitmap)
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

    private fun sendCategoryData(categoryIcon: String){
        val category = binding.categoryNameTxt.text.toString()
        if (category.isNotEmpty()){
            val dataId = database.push().key?:return
            val id = CategoryInsertData(dataId, category, categoryIcon)
            database.child(dataId).setValue(id).addOnSuccessListener {
                Toast.makeText(this,"Category Created Successfully",Toast.LENGTH_LONG).show()
                binding.categoryNameTxt.text.clear()
                binding.categoryNamePreviewTxt.text = null
                binding.categoryIconCustom.setImageDrawable(null)
            }

        }else{
            Toast.makeText(this, "Please enter text", Toast.LENGTH_LONG).show()
        }
    }
}