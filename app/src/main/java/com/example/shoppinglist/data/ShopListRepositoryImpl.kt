package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.shoppinglist.data.database.AppDataBase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()

    override fun getShopList(): LiveData<List<ShopItem>> =
        /*MediatorLiveData<List<ShopItem>>().apply{
            addSource(shopListDao.getShopList()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }*/
        Transformations.map(shopListDao.getShopList()) {
            mapper.mapListDbModelToListEntity(it)
        }

    override suspend fun addShopItem(item: ShopItem) {
        val dbItem = mapper.mapEntityToDbModel(item)
        shopListDao.addShopItem(dbItem)
    }

    override suspend fun deleteShopItem(item: ShopItem) {
        shopListDao.deleteShopItem(item.id)
    }

    override suspend fun editShopItem(item: ShopItem) {
        //Скопирована реализация addShopItem, потому что при добавлении элемент заменить существующий
        val dbItem = mapper.mapEntityToDbModel(item)
        shopListDao.addShopItem(dbItem)
    }

    override suspend fun getShopItemById(shopItemId: Int): ShopItem {
        val dbItem = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbItem)
    }
}