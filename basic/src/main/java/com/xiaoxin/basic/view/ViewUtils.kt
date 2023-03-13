package com.xiaoxin.basic.view

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author: Admin
 * @date: 2021-09-02
 */
object ViewUtils {

    // dp 转 px
    fun dp2px(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics)
    }

    // px 转 dp
    fun px2dp(px: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), Resources.getSystem().displayMetrics)
    }

    // sp 转 px
    fun sp2px(sp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), Resources.getSystem().displayMetrics)
    }
}