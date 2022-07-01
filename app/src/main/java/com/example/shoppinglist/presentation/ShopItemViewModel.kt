package com.example.shoppinglist.presentation

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl


    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)


    fun addShopItem(inputName: String, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            val newItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(newItem)
        }
    }

    fun getShopItemById(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItemById(shopItemId)
    }

    fun editShopItem(inputName: String, inputCount: String?) {
        //TODO: Переделать реализацию. Она неактуально
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            val newItem = ShopItem(name, count, true)
            editShopItemUseCase.editShopItem(newItem)
        }

    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return if (inputCount?.isDigitsOnly() == true) inputCount.toInt() else 0
        /*try {
            inputCount?.trim()?.toInt() ?: 0
        }
        catch (e: Exception){
            0
        }*/
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            // TODO: Отобразить ошибку ввода имени
            result = false
        }
        if (count <= 0) {
            // TODO: Отобразить ошибку ввода количества
            result = false
        }
        return result
    }
}