package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl: ShopListRepository {

    //Пока временная реализация через список, вместо БД
    private val shopList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    init{
        addShopItem(ShopItem("lol", 5, true))
        addShopItem(ShopItem("kek", 2, true))
        deleteShopItem(getShopItemById(0))
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun addShopItem(item: ShopItem) {
        if(item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        shopList.add(item)
        updateListLD()
    }

    override fun deleteShopItem(item: ShopItem) {
        shopList.remove(item)
        updateListLD()
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

    private fun updateListLD(){
        shopListLD.value = shopList.toList()
    }
}