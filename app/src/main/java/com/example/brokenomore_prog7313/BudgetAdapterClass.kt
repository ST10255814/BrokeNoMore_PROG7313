package com.example.brokenomore_prog7313

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Base64

class BudgetAdapterClass(
    private val dataList:ArrayList<BudgetDisplayData>,
    private val onItemClick: (BudgetDisplayData) -> Unit
) : RecyclerView.Adapter<BudgetAdapterClass.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItems = dataList[position]

        val bitmap = decodeBase64ToBitmap(currentItems.categoryImage)
        if (bitmap != null) {
            holder.rvImageView.setImageBitmap(bitmap)
        } else {
            holder.rvImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.rvCategoryName.text = currentItems.budgetName
        holder.rvAmount.text = "R %.2f".format(currentItems.maxAmount)

        holder.itemView.setOnClickListener {
            onItemClick(currentItems)
        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val rvImageView: ImageView = itemView.findViewById(R.id.category_icon_budget)
        val rvCategoryName: TextView = itemView.findViewById(R.id.budget_name)
        val rvAmount: TextView = itemView.findViewById(R.id.budget_amount)
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
}