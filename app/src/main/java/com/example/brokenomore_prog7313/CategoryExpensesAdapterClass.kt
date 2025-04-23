package com.example.brokenomore_prog7313

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryExpensesAdapterClass(private val dataList:ArrayList<CategoryExpensesDataClass>):
RecyclerView.Adapter<CategoryExpensesAdapterClass.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItems = dataList[position]
        holder.rvImageView.setImageResource(currentItems.categoryImage)
        holder.rvCategoryName.text = currentItems.categoryName
        holder.rvAmount.text = currentItems.amount.toString()
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val rvImageView: ImageView = itemView.findViewById(R.id.category_icon_budget)
        val rvCategoryName: TextView = itemView.findViewById(R.id.budget_name)
        val rvAmount: TextView = itemView.findViewById(R.id.budget_amount)
    }
}