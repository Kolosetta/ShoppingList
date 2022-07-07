package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: ShopItemViewModel
    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        validateIntent()

        //Создаем фрагмент вручную только при первом запуске активити
        if(savedInstanceState == null) {
            launchRightMode()
        }

        viewModel.shouldCloseScreen.observe(this){
            onEditFinished()
        }
    }

    private fun validateIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Отсутствует параметр EXTRA_SCREEN_MODE в Intent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != ADD_MODE && mode != EDIT_MODE) {
            throw RuntimeException("Неизвестный screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == EDIT_MODE) {
            if (!intent.hasExtra(EXTRA_ITEM_ID)) {
                throw RuntimeException("Отсутствует параметр EXTRA_ITEM_ID")
            }
            shopItemId = intent.getIntExtra(EXTRA_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode(){
        val fragment = when(screenMode){
            EDIT_MODE -> ShopItemFragment.newInstanceEditItem(shopItemId)
            ADD_MODE -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Неизвестный тип фрагмента")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "mode"
        private const val EXTRA_ITEM_ID = "item_id"
        private const val EDIT_MODE = "edit_mode"
        private const val ADD_MODE = "add_mode"
        private const val UNKNOWN_MODE = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, ADD_MODE)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EDIT_MODE)
            intent.putExtra(EXTRA_ITEM_ID, itemId)
            return intent
        }
    }

    override fun onEditFinished() {
        finish()
    }
}