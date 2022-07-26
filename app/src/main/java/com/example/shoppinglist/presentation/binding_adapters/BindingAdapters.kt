package com.example.shoppinglist.presentation.binding_adapters

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("nameText")
fun bindNameText(textInputEditText: TextInputEditText, name: String?){
    if(name != null){
        textInputEditText.setText(name)
    }
}

@BindingAdapter("countText")
fun bindCountText(textInputEditText: TextInputEditText, count: Int){
    if(count != 0){
        textInputEditText.setText(count.toString())
    }
}