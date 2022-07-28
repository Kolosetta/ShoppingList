package com.example.shoppinglist.data

import com.example.shoppinglist.data.database.ShopItemDbModel
import com.example.shoppinglist.domain.ShopItem

class ShopListMapper {

    //Возвращает объект ShopItemDbModel созданный из ShopItem
    fun mapEntityToDbModel(item: ShopItem) : ShopItemDbModel{
        return ShopItemDbModel(
            id = item.id,
            name = item.name,
            count = item.count,
            enabled = item.enabled
        )
    }

    //Возвращает объект ShopItem созданный из ShopItemDbModel
    fun mapDbModelToEntity(item: ShopItemDbModel) : ShopItem{
        return ShopItem(
            id = item.id,
            name = item.name,
            count = item.count,
            enabled = item.enabled
        )
    }

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) : List<ShopItem>{
        return list.map{
            mapDbModelToEntity(it)
        }
    }

}