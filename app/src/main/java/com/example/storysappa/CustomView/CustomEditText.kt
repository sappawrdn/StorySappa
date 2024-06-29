package com.example.storysappa.CustomView

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditText : TextInputEditText {

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parentLayout = parent.parent as TextInputLayout

                when(inputType) {
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT -> {
                        val email = s.toString()
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            parentLayout.error = "Format Email Salah"
                        }else {
                            parentLayout.error = null
                        }
                    }

                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT -> {
                        val password = s.toString()
                        if (password.length < 8){
                            parentLayout.error = "Password tidak boleh kurang dari 8 karakter"
                        }else {
                            parentLayout.error = null
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}