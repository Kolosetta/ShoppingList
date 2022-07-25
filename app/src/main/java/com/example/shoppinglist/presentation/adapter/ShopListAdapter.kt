package com.example.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ItemShopDisabledBinding
import com.example.shoppinglist.databinding.ItemShopEnabledBinding
import com.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

//Используем ListAdapter вместо RVAdapter. Он позволяет удобно работать с DiffCallback для перерисовки списка
//Также скрывает в себе реализацию самого списка айтемов
class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var longClickListener: ((ShopItem) -> Unit)? = null
    var clickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        //Инфлайтим view из разных макетов, в зависимости от viewType
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("неизвестный тип view: $viewType")
        }
        //Создаем binding не на основе конкретного класса, а на основе макета layout
        //Приводя binding к родительскому классу ViewDataBinding
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
            layout,
            parent,
            false)
        //val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopListItem = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            longClickListener?.invoke(shopListItem)
            true
        }
        binding.root.setOnClickListener{
            clickListener?.invoke(shopListItem)
        }
        //Приводим binding к конкретному типу, чтобы работать с элементами его view
        when(binding){
            is ItemShopDisabledBinding -> {
                binding.itemName.text = shopListItem.name
                binding.itemCount.text = shopListItem.count.toString()}
            is ItemShopEnabledBinding -> {
                binding.itemName.text = shopListItem.name
                binding.itemCount.text = shopListItem.count.toString()}
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


