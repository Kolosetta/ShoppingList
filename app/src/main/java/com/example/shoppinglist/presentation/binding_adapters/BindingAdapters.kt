package com.example.shoppinglist.presentation.binding_adapters

import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//Устанавливает имя айтема в поле
@BindingAdapter("nameText")
fun bindNameText(textInputEditText: TextInputEditText, name: String?){
    if(name != null){
        textInputEditText.setText(name)
    }
}

//Устанавливает количество айтема в поле
@BindingAdapter("countText")
fun bindCountText(textInputEditText: TextInputEditText, count: Int){
    if(count != 0){
        textInputEditText.setText(count.toString())
    }
}

//Устанавливает текст ошибки над полем имени
@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean){
    when (isError) {
        true -> textInputLayout.error =  textInputLayout.context.getString(R.string.need_correct_name)
        false -> textInputLayout.error = null
    }
}

//Устанавливает текст ошибки над полем количества
@BindingAdapter("errorInputCount")
fun bindErrorInputCount(countInputLayout: TextInputLayout, isError: Boolean){
    when (isError) {
        true -> countInputLayout.error = countInputLayout.context.getString(R.string.need_correct_count)
        false -> countInputLayout.error = null
    }
}