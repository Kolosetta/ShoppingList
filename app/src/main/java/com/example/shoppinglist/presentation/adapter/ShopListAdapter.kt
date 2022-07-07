package com.example.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

//Используем ListAdapter вместо RVAdapter. Он позволяет удобно работать с DiffCallback для перерисовки списка
//Также скрывает в себе реализацию самого списка айтемов
class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    //Функции в переменных, которые задаются снаружи, в MainActivity
    var longClickListener: ((ShopItem) -> Unit)? = null
    var clickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        //Инфлайтим view из разных макетов, в зависимости от viewType
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("неизвестный тип view: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopListItem = getItem(position)
        holder.nameTextView.text = shopListItem.name
        holder.countTextView.text = shopListItem.count.toString()
        holder.view.setOnLongClickListener {
            longClickListener?.invoke(shopListItem)
            true
        }
        holder.view.setOnClickListener{
            clickListener?.invoke(shopListItem)
        }

    }

    override fun getItemViewType(position: Int): Int {
        //Возвращаем viewType, в зависимости того, enabled ли заметка по текущей позиции
        val currentItem = getItem(position)
        return if (currentItem.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }
}


