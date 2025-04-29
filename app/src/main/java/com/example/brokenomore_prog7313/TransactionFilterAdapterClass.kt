package com.example.brokenomore_prog7313

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.brokenomore_prog7313.TransactionAdapterClass.ViewHolderClass

class TransactionFilterAdapterClass(
    private val transactionList: ArrayList<TransactionDataClass>
) : Adapter<TransactionFilterAdapterClass.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = transactionList[position]

        val originalDate = currentItem.startDate
        val formattedDate = formatDate(originalDate)

        holder.transactionDate.text = formattedDate
        holder.transactionCategory.text = currentItem.description
        holder.transactionAmount.text = "R%.2f".format(currentItem.amount)
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView){
        val transactionDate : TextView = itemView.findViewById(R.id.transactionDateTxt)
        val transactionCategory : TextView = itemView.findViewById(R.id.transactionDescriptionTxt)
        val transactionAmount : TextView = itemView.findViewById(R.id.transactionAmountTxt)
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(dateString)
            val outputFormat = java.text.SimpleDateFormat("dd-MMM")
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }
}