package com.xiaoxin.basic.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import com.xiaoxin.basic.R

/**
 * @author: Admin
 * @date: 2021-10-11
 */
class BaseDialog : Dialog {

    var gravity: Int? = Gravity.CENTER

    constructor(context: Context, view: View, gravity: Int? = Gravity.CENTER) : super(context) {
        this.gravity=gravity
        setContentView(view = view)
    }

    constructor(context: Context, themeResId: Int, view: View, gravity: Int? = Gravity.CENTER) : super(context, themeResId) {
        this.gravity=gravity
        setContentView(view = view)
    }

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?,
        view: View,
        gravity: Int? = Gravity.CENTER
    ) : super(
        context,
        cancelable,
        cancelListener
    ) {
        this.gravity=gravity
        setContentView(view = view)
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        val window = window
        window!!.setWindowAnimations(R.style.dialog_nim)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        val wlp = window.attributes
        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        //获取屏幕宽
        wlp.width = displayMetrics.widthPixels
        wlp.gravity = gravity!!
        window.attributes = wlp
        setCanceledOnTouchOutside(false)
    }

}