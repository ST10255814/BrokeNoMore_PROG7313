package com.example.brokenomore_prog7313

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionExpenseAdapterClass (private val dataList:ArrayList<TransactionExpenseDataClass>):
    RecyclerView.Adapter<TransactionExpenseAdapterClass.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItems = dataList[position]
        holder.rvDate.text = currentItems.transactionDate
        holder.rvTransactionDescription.text = currentItems.transactionDescription
        holder.rvTransactionAmount.text = currentItems.transactionAmount
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvDate: TextView = itemView.findViewById(R.id.transactionDateTxt)
        val rvTransactionDescription: TextView = itemView.findViewById(R.id.transactionDescriptionTxt)
        val rvTransactionAmount: TextView = itemView.findViewById(R.id.transactionAmountTxt)
    }
}