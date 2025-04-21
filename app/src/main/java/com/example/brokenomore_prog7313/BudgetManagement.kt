package com.example.brokenomore_prog7313

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brokenomore_prog7313.databinding.ActivityBudgetBinding

class BudgetManagement : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}