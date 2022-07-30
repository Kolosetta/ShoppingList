package com.example.shoppinglist.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun getShopItemById(shopItemId: Int): ShopItem{
        return shopListRepository.getShopItemById(shopItemId)
    }
}