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

    constructor(context: Context, view: View) : super(context) {
        setContentView(view)
    }

    constructor(context: Context, themeResId: Int, view: View) : super(context, themeResId) {
        setContentView(view)
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?, view: View) : super(
        context,
        cancelable,
        cancelListener
    ) {
        setContentView(view)
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
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp
        setCanceledOnTouchOutside(false)
    }

}