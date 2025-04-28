package com.example.brokenomore_prog7313

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionLogAdapterClass (
    private val dataList:ArrayList<TransactionDisplayDataClass>,
    private val onItemClick: (TransactionDisplayDataClass) -> Unit
) : RecyclerView.Adapter<TransactionLogAdapterClass.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_transactions_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItems = dataList[position]

        val originalDate = currentItems.transactionStartDate
        val formattedDate = formatDate(originalDate)

        holder.rvTransactionLogDate.text = formattedDate

        val bitmap = decodeBase64ToBitmap(currentItems.categoryImage)
        if (bitmap != null) {
            holder.rvCategoryImage.setImageBitmap(bitmap)
        } else {
            holder.rvCategoryImage.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.rvTransactionLogDescription.text = currentItems.transactionDescription

        holder.rvTransactionLogAmount.text = "-R%.2f".format(currentItems.transactionAmount)

        holder.itemView.setOnClickListener {
            onItemClick(currentItems)
        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTransactionLogDate: TextView = itemView.findViewById(R.id.transactionLogDateTxt)
        val rvCategoryImage: ImageView = itemView.findViewById(R.id.category_transaction_icon)
        val rvTransactionLogDescription: TextView = itemView.findViewById(R.id.transactionLogDescriptionTxt)
        val rvTransactionLogAmount: TextView = itemView.findViewById(R.id.transactionLogAmountTxt)
    }

    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("dd-MM-yyyy") // From Firebase
            val date = inputFormat.parse(dateString)
            val outputFormat = java.text.SimpleDateFormat("dd-MMM") // What you want
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString // fallback to original if parsing fails
        }
    }
}