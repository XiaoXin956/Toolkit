package com.xiaoxin.common.utils

import java.util.regex.Pattern

/**
 * @author: Admin
 * @date: 2021-08-24
 */
class StringUtils {

    // url 地址正则
    private val URL = Pattern.compile("[a-zA-z]+://[^\\s]*")

    /**
     * 判断是否为网页地址
     */
    fun isUrl(url: String): Boolean {
        return if (url.isEmpty()) {
            false
        } else {
            URL.matcher(url).matches()
        }
    }


}