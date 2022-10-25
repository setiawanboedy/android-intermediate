package com.example.ourstory.ui.components

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.example.ourstory.R

class EmailEdittext : AppCompatEditText {

    constructor(context: Context) : super(context) {init()}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){init()}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){init()}

    private fun init(){
        background = null
        hint = resources.getString(R.string.email_address)

        doAfterTextChanged {
            if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()){
                error = resources.getString(R.string.invalid_email)
            }
        }

    }
}