package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var editTextName: EditText
    private lateinit var editTextCount: EditText
    private lateinit var saveBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }


    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        launchRightMode()
        setupErrorsHandler()
        viewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }


    private fun initViews() {
        tilName = findViewById(R.id.text_input_layout)
        tilCount = findViewById(R.id.count_input_layout)
        editTextName = findViewById(R.id.edit_text_name)
        editTextCount = findViewById(R.id.edit_text_count)
        saveBtn = findViewById(R.id.save_button)
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

    private fun setupErrorsHandler(){
        viewModel.errorInputName.observe(this){
            when(it){
                true -> tilName.error = "Необходимо ввести корректное название"
                false -> tilName.error = null
            }
        }

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
        })

        viewModel.errorInputCount.observe(this){
            when(it){
                true -> tilCount.error = "Необходимо ввести корректное количество"
                false -> tilCount.error = null
            }
        }

        editTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }
        })
    }

    private fun launchEditMode(){
        viewModel.getShopItemById(shopItemId)
        viewModel.shopItem.observe(this){
            editTextName.setText(it.name)
            editTextCount.setText(it.count.toString())
        }

        saveBtn.setOnClickListener{
            viewModel.editShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun launchAddMode(){
        saveBtn.setOnClickListener{
            viewModel.addShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun launchRightMode(){
        when(screenMode){
            EDIT_MODE -> launchEditMode()
            ADD_MODE -> launchAddMode()
        }
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
}