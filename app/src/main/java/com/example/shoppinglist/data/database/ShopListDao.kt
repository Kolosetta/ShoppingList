package com.example.shoppinglist.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(item: ShopItemDbModel)

    @Query("DELETE FROM shop_items WHERE id=:itemId")
    suspend fun deleteShopItem(itemId: Int)

    @Query("SELECT * FROM shop_items WHERE id=:itemId")
    suspend fun getShopItem(itemId: Int): ShopItemDbModel

}