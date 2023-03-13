package com.xiaoxin.basic.date

import java.util.*


/**
 * @author: Admin
 * @date: 2022-04-14
 */
class DateBean() {

    companion object {
        const val ITEM_TYPE_DAY = 1 //日期item
        const val ITEM_TYPE_MONTH = 2 //月份item

        const val ITEM_STATE_BEGIN_DATE = 1 //开始日期
        const val ITEM_STATE_END_DATE = 2 //结束日期
        const val ITEM_STATE_SELECTED = 3 // 选中状态
        const val ITEM_STATE_NORMAL = 4 // 正常状态
        const val ITEM_STATE_NO_CHECK = 5 // 禁止选择
    }

    var itemType = ITEM_TYPE_DAY
    var date: Date? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null
    var itemState: Int? = ITEM_STATE_NORMAL

    override fun toString(): String {
        return "DateBean(item 类型 =$itemType, 日期=${date?.let { FormatYYYYMMDD.format(it) }},年 = $year , 月份=$month,日=$day, item 状态=$itemState)"
    }


}

class WeekBean {

    var weekLab: String? = null

}
