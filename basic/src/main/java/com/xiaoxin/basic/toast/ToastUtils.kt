package com.xiaoxin.basic.toast

import android.app.Application
import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 * @author: Admin
 * @date: 2021-08-23
 */
object ToastUtils {

    private var context: Application? = null
    private var toast: Toast? = null

    fun init(application: Application) {
        if (context == null) {
            this.context = application
        }
    }

    // 显示
    @JvmStatic
    fun showShort(msg: CharSequence) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast?.setText(msg)
        toast?.show()
    }

    // 指定位置显示
    @JvmStatic
    fun showShortPosition(msg: CharSequence, xPosition: Int, yPosition: Int) {
        if (toast == null) {
            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
            toast?.setText(msg)
        } else {
            toast?.let {
                it.cancel()
                toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
                it.setText(msg)
            }
        }
        toast?.let {
            it.setGravity(Gravity.CENTER, xPosition, yPosition)
            it.show()
        }

    }

}
