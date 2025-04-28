package com.example.brokenomore_prog7313

import java.io.Serializable

data class TransactionDisplayDataClass(
    var transactionStartDate: String = "",
    var transactionEndDate: String = "",
    var categoryImage: String = "",
    var categoryName: String = "",
    var transactionDescription: String = "",
    var transactionAmount: Double = 0.0,
    var transactionReceipt: String = ""
) : Serializable
