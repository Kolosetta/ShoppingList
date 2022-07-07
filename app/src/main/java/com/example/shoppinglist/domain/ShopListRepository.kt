package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

//Список всех методов, Которые необходимо реализовать в data слое
interface ShopListRepository {
    fun getShopList(): LiveData<List<ShopItem>>
    fun addShopItem(item: ShopItem)
    fun deleteShopItem(item: ShopItem)
    fun editShopItem(item: ShopItem)
    fun getShopItemById(shopItemId: Int): ShopItem
}