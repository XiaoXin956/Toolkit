package com.xiaoxin.common.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: Admin
 * @date: 2021-08-24
 */
class DataTimeUtils {

    /**
     * 日期转字符串
     */
    fun dataToString(date: Date, formatType: String): String? {
        synchronized(date) {
            val simpleDateFormat: SimpleDateFormat? = try {
                SimpleDateFormat(formatType, Locale.CHINA);
            } catch (ex: Exception) {
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            }
            return simpleDateFormat?.format(date);
        }
    }


    /**
     * 字符串转日期
     */
    fun stringToData(dateStr: String, formatType: String): Date {
        synchronized(dateStr){
            // 判断是否为时间戳
            return if (dateStr.contains("-")) {
                // 日期格式
                SimpleDateFormat(formatType, Locale.CHINA).parse(dateStr);
            } else {
                //时间戳格式
                if (dateStr.length == 10) {
                    dateStr + "000"
                }
                Date(java.lang.Long.valueOf(dateStr))
            }
        }
    }


}