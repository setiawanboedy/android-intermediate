package com.example.ourstory.ui.components

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.ourstory.R
import com.example.ourstory.session.Preferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmPasswordEdittext : AppCompatEditText {

    @Inject
    lateinit var prefs: Preferences

    private var char = ""

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        background = null
        hint = resources.getString(R.string.password_char)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                char = p0.toString()
                error =
                    if (char != prefs.getPw()) context.getString(R.string.pw_not_match) else null
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}