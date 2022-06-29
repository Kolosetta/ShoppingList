package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopItemViewHolder>() {

    val list = listOf<ShopItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_enabled, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopListItem = list[position]
        holder.nameTextView.text = shopListItem.name
        holder.countTextView.text = shopListItem.count.toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val nameTextView = view.findViewById<TextView>(R.id.item_name)
    val countTextView = view.findViewById<TextView>(R.id.item_count)
}