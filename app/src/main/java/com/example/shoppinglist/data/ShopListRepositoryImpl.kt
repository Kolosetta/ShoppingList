package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl: ShopListRepository {

    //Пока временная реализация через список, вместо БД
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    override fun getShopList(): List<ShopItem> {
        return shopList.toMutableList()
    }

    override fun addShopItem(item: ShopItem) {
        if(item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        shopList.add(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        shopList.remove(item)
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItemById(item.id)
        shopList.remove(oldItem)
        addShopItem(item)
    }

    override fun getShopItemById(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Элемент с id $shopItemId не существует")
    }
}