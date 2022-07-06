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

class ShopItemFragment(
    private val screenMode: String = UNKNOWN_MODE,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID
) : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var editTextName: EditText
    private lateinit var editTextCount: EditText
    private lateinit var saveBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateParams()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        setupErrorsHandler()
        launchRightMode()
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            activity?.onBackPressed()
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.text_input_layout)
        tilCount = view.findViewById(R.id.count_input_layout)
        editTextName = view.findViewById(R.id.edit_text_name)
        editTextCount = view.findViewById(R.id.edit_text_count)
        saveBtn = view.findViewById(R.id.save_button)
    }

    private fun validateParams() {
        if (screenMode != EDIT_MODE && screenMode != ADD_MODE) {
            throw RuntimeException("Некорректный параметр screen_mode")
        }
        if(screenMode == EDIT_MODE && shopItemId == ShopItem.UNDEFINED_ID){
            throw RuntimeException("Отсутствует параметр EXTRA_ITEM_ID")
        }

    }

    private fun setupErrorsHandler(){
        viewModel.errorInputName.observe(viewLifecycleOwner){
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

        viewModel.errorInputCount.observe(viewLifecycleOwner){
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
        viewModel.shopItem.observe(viewLifecycleOwner){
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
        private const val EDIT_MODE = "edit_mode"
        private const val ADD_MODE = "add_mode"
        private const val UNKNOWN_MODE = ""

        fun newInstanceAddItem(): ShopItemFragment{
            return ShopItemFragment(ADD_MODE)
        }

        fun newInstanceEditItem(): ShopItemFragment{
            return ShopItemFragment(EDIT_MODE)
        }
    }
}