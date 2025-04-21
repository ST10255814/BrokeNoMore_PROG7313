package com.example.brokenomore_prog7313

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityCategoryCreationBinding

class CategoryCreation : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryCreationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}