package com.example.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.shoppinglist.domain.ShopItem

class ShopItemDiffCallback : ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}