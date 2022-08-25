package com.xiaoxin.basic.currency

//import android.icu.text.DecimalFormat
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * 货币工具类
 * @author: Admin
 * @date: 2021-08-24
 */
object CurrencyUtils {

    /**
     * 金额加
     */
    fun amountAdd(amount1: Long, amount2: Long, flag: String): Long {
        return when (flag) {
            "+" -> BigDecimal(amount1).add(BigDecimal(amount2)).toLong()
            "-" -> BigDecimal(amount1).subtract(BigDecimal(amount2)).toLong()
            "*" -> BigDecimal(amount1).multiply(BigDecimal(amount2)).toLong()
            "/" -> BigDecimal(amount1).divide(BigDecimal(amount2),BigDecimal.ROUND_HALF_UP).toLong()
            else -> 0
        }
    }

    /**
     * 保留两位小数
     */
    fun keepTwoDecimal(amount: String): String {
        if (amount.contains(".")) {
            return amount
        }
        return DecimalFormat("0.00").format(amount.toDouble())

    }

    /**
     * 转千分位
     */
    fun amountMicrometer(amount: String): String {
        return when (amount) {
            "." -> amount
            "0" -> amount
            else -> {
                val decimalFormat = DecimalFormat(",###,##0.00")
                decimalFormat.format(BigDecimal(amount))
            }
        }

    }

}