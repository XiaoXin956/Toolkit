package com.xiaoxin.basic.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

inline fun EditText.addTextChangeListener(init: TextChange.() -> Unit) {
    val listener = TextChange()
    listener.init()
    addTextChangedListener(listener)
}

class TextChange : TextWatcher {

    private var beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var textChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var afterTextChanged: ((Editable?) -> Unit)? = null

    fun onBeforeTextChanged(method: ((CharSequence?, Int, Int, Int) -> Unit)?){
        beforeTextChanged =method
    }

    fun onTextChanged(method: ((CharSequence?, Int, Int, Int) -> Unit)?){
        textChanged = method
    }

    fun onAfterTextChanged(method: ((Editable?) -> Unit)?){
        afterTextChanged =method
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }

}

