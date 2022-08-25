package com.xiaoxin.common.widget

import android.view.View

inline fun View.click(init: ViewClickListener.() -> Unit) {
    val viewClick = ViewClickListener()
    viewClick.init()
    setOnClickListener(viewClick)
    setOnLongClickListener(viewClick)
}

class ViewClickListener : View.OnClickListener, View.OnLongClickListener {

    private var click: ((View) -> Unit)? = null
    private var longClick: ((View) -> Unit)? = null

    fun onClick(method: ((View) -> Unit)? = null) {
        click = method
    }

    fun onLongClick(method: ((View) -> Unit)? = null) {
        longClick = method
    }

    override fun onClick(v: View?) {
        click?.invoke(v!!)
    }

    override fun onLongClick(v: View?): Boolean {
        longClick?.invoke(v!!)
        return true
    }

}


