package com.xiaoxin.common.utils

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.widget.Toast

/**
 * @author: Admin
 * @date: 2021-08-23
 */
class ToastUtils {

    companion object {

        private var toast: Toast? = null
        // 显示
        @JvmStatic
        fun showShort(context: Context?, msg: CharSequence) {
            if (toast != null) {
                toast?.cancel()
            }
            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            toast?.setText(msg)
            toast?.show()
        }
        // 指定位置显示
        @JvmStatic
        fun showShortPosition(context: Context, msg: CharSequence, xPosition: Int, yPosition: Int) {
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

}