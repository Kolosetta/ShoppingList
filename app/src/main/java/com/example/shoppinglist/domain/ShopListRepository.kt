package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    fun getShopList(): LiveData<List<ShopItem>>
    suspend fun addShopItem(item: ShopItem)
    suspend fun deleteShopItem(item: ShopItem)
    suspend fun editShopItem(item: ShopItem)
    suspend fun getShopItemById(shopItemId: Int): ShopItem
}