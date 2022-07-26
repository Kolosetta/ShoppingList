package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemFragment() : Fragment() {

    private var screenMode: String = UNKNOWN_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private lateinit var binding: FragmentShopItemBinding
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener
    private lateinit var viewModel: ShopItemViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Если родительская активити реализует OnEditingFinishedListener
        if(context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }
        else{
            throw RuntimeException("Родительская активити фрагмента ${this.javaClass} обязана реализовавать OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateParams()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShopItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupErrorsHandler()
        launchRightMode()
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditFinished()
        }
    }

    private fun validateParams() {
        val args = requireArguments() //Получаем параметры из пришедшего бандла
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Отсутствует параметр SCREEN_MODE")
        }
        screenMode = args.getString(SCREEN_MODE).toString()

        if (screenMode != EDIT_MODE && screenMode != ADD_MODE) {
            throw RuntimeException("Некорректный параметр SCREEN_MODE")
        }

        if (screenMode == EDIT_MODE) {
            if (!args.containsKey(ITEM_ID)) {
                throw RuntimeException("Отсутствует параметр ITEM_ID")
            }
            shopItemId = args.getInt(ITEM_ID, ShopItem.UNDEFINED_ID)
        }

    }

    private fun setupErrorsHandler() {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
        })

        binding.editTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItemById(shopItemId)

        binding.saveButton.setOnClickListener {
            viewModel.editShopItem(binding.editTextName.text?.toString(), binding.editTextCount.text?.toString())
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addShopItem(binding.editTextName.text?.toString(), binding.editTextCount.text?.toString())
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            ADD_MODE -> launchAddMode()
            EDIT_MODE -> launchEditMode()
        }
    }

    companion object {
        private const val SCREEN_MODE = "mode"
        private const val ITEM_ID = "item_id"
        private const val EDIT_MODE = "edit_mode"
        private const val ADD_MODE = "add_mode"
        private const val UNKNOWN_MODE = ""

        @JvmStatic
        fun newInstanceAddItem(): ShopItemFragment {
            val bundle = Bundle().apply {
                putString(SCREEN_MODE, ADD_MODE)
            }
            return ShopItemFragment().apply {
                arguments = bundle
            }
        }

        @JvmStatic
        fun newInstanceEditItem(itemId: Int): ShopItemFragment {
            val bundle = Bundle().apply {
                putString(SCREEN_MODE, EDIT_MODE)
                putInt(ITEM_ID, itemId)
            }
            return ShopItemFragment().apply {
                arguments = bundle
            }
        }
    }

    interface OnEditingFinishedListener{
        fun onEditFinished()
    }
}