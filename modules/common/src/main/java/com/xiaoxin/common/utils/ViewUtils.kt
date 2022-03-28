package com.xiaoxin.common.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author: Admin
 * @date: 2021-09-02
 */
object ViewUtils {
    /**
     * dp 转 px
     * @param dp
     * @return
     */
    fun dp2px(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics)
    }

    fun px2dp(px: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), Resources.getSystem().displayMetrics)
    }
}