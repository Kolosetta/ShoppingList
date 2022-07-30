package com.example.shoppinglist.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun addShopItem(item: ShopItem){
        shopListRepository.addShopItem(item)
    }
}