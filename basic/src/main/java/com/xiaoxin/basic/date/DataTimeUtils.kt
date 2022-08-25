package com.xiaoxin.basic.date

import java.text.SimpleDateFormat
import java.util.*

val FormatYYYYMMDD = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
val FormatYYYYMMDDHHMMSS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

val DateToString: ((Any, SimpleDateFormat) -> String) = { time, format ->
    when (time) {
        is String -> {
            time
        }
        is Date -> {
            FormatYYYYMMDD.format(time)
        }
        else -> {
            // 异常就是当前时间
            FormatYYYYMMDD.format(Date())
        }
    }
}

class DataTimeUtils {


    /**
     * 日期转字符串
     */
    fun dataToString(date: Date, formatType: String): String? {
        synchronized(date) {
            val simpleDateFormat: SimpleDateFormat? = try {
                SimpleDateFormat(formatType, Locale.CHINA)
            } catch (ex: Exception) {
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            }
            return simpleDateFormat?.format(date)
        }
    }


    /**
     * 字符串转日期
     */
    fun stringToData(dateStr: String, formatType: String): Date {
        synchronized(dateStr) {
            // 判断是否为时间戳
            return if (dateStr.contains("-")) {
                // 日期格式
                SimpleDateFormat(formatType, Locale.CHINA).parse(dateStr)!!
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