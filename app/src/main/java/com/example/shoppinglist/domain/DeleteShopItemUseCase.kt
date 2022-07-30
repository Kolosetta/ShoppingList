package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun deleteShopItem(item: ShopItem){
        shopListRepository.deleteShopItem(item)
    }
}