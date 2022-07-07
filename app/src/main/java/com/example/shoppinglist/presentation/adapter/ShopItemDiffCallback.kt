package com.example.shoppinglist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.shoppinglist.domain.ShopItem

//Реализация ItemCallback, в которой описано, как сравнивать элементы
//Она нужна, чтобы обновлять RV в зависимости от того, какие элементы изменились
//а не просто вызывать notifyDataSetChanged
class ShopItemDiffCallback : ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}