package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    //Пока временная реализация через список, вместо БД
    private val shopList = sortedSetOf<ShopItem>({
            //Анонимная реализация компаратора, чтобы список был сортированным
            o1, o2 -> o1.id.compareTo(o2.id)
    })
    //Список shopList в контейнере LiveData, чтобы на него можно было подписаться
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    //Просто заполнение списка начальными значениями
    init{
        for(i in 1 until 10)
        addShopItem(ShopItem("lol", 5, Random.nextBoolean()))
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun addShopItem(item: ShopItem) {
        //Присвоение новому айтему if
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

    //Обновление LiveData версии списка
    private fun updateListLD(){
        shopListLD.value = shopList.toList()
    }
}