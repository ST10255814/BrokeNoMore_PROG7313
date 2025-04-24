package com.example.brokenomore_prog7313

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionLogAdapterClass (private val dataList:ArrayList<TransactionLogDataClass>):
    RecyclerView.Adapter<TransactionLogAdapterClass.ViewHolderClass>() {

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTransactionLogDate: TextView = itemView.findViewById(R.id.transactionLogDateTxt)
        val rvCategoryImage: ImageView = itemView.findViewById(R.id.category_transaction_icon)
        val rvTransactionLogDescription: TextView = itemView.findViewById(R.id.transactionLogDescriptionTxt)
        val rvTransactionLogAmount: TextView = itemView.findViewById(R.id.transactionLogAmountTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_transactions_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItems = dataList[position]
        holder.rvTransactionLogDate.text = currentItems.transactionLogDate
        holder.rvCategoryImage.setImageResource(currentItems.categoryLogImage)
        holder.rvTransactionLogDescription.text = currentItems.transactionLogDescription
        holder.rvTransactionLogAmount.text = currentItems.transactionLogAmount
    }
}