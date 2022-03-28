package com.xiaoxin.toolkit

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

inline fun TextView.textChangedListener(init: _TextChanged.() -> Unit) {
    val listener = _TextChanged()
    listener.init()
    addTextChangedListener(listener)
}

class _TextChanged : TextWatcher {

    var beforeTextChanged : ((s: CharSequence?, start: Int, count: Int, after: Int)->Unit)?=null
    var onTextChanged : ((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)?=null
    var afterTextChanged : ((s: Editable?)->Unit)?=null

    fun beforeTextChangedListener(method:((s: CharSequence?, start: Int, count: Int, after: Int)->Unit)){
        beforeTextChanged
    }

    fun onTextChangedListener(method:((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)){
        onTextChanged = method
    }

    fun afterTextChangedListener(method:((s: Editable?)->Unit)){
        afterTextChanged = method
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }

}
