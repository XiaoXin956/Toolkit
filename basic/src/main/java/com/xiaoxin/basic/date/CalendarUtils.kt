package com.xiaoxin.basic.date

import android.util.Log
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author: Admin
 * @date: 2022-04-14
 */
class CalendarUtils {

    companion object {
        const val FIRST_DAY_SUNDAY = 7
        const val FIRST_DAY_MONDAY = 1
    }

    private val startArray: (Int) -> Array<Int> = {
        when (it) {
            1 -> {
                arrayOf(6, 0, 1, 2, 3, 4, 5)
            }
            7 -> {
                arrayOf(0, 1, 2, 3, 4, 5, 6)
            }
            else -> {
                arrayOf(0, 1, 2, 3, 4, 5, 6)
            }
        }
    }

    private val endArray: (Int) -> Array<Int> = {
        when (it) {
            1 -> {
                arrayOf(0, 6, 5, 4, 3, 2, 1)
            }
            7 -> {
                arrayOf(6, 5, 4, 3, 2, 1, 0)
            }
            else -> {
                arrayOf(6, 0, 1, 2, 3, 4, 5)
            }
        }
    }

    /**
     * 当前时间
     */
    private var currentDate: (Any) -> Date = {
        when (it) {
            is String -> {
                try {
                    FormatYYYYMMDD.parse(it)!!
                } catch (ex: Exception) {
                    Date()
                }
            }
            is Date -> {
                it
            }
            else -> {
                Date()
            }
        }
    }

    /**
     * 开始日期
     */
    private var startDate: (Any) -> Date = {
        when (it) {
            is String -> {
                try {
                    FormatYYYYMMDD.parse(it)!!
                } catch (ex: Exception) {
                    Date()
                }
            }
            is Date -> {
                it
            }
            else -> {
                Date()
            }
        }
    }

    /**
     * 结束时间
     */
    private var endDate: (Any?) -> Date = {
        when (it) {
            is String -> {
                try {
                    FormatYYYYMMDD.parse(it)!!
                } catch (ex: Exception) {
                    Date()
                }
            }
            is Date -> {
                it
            }
            else -> {
                Date()
            }
        }

    }

    /**
     * 生成日历数据，周一还是周日开始
     * @param startTimeRes 开始时间
     * @param endTimeRes 结束时间，null 为当前时间
     * @param firstWeekDay 1>周一开始，7>周日开始
     * @return List<DateBean> 日期数据
     */
    fun createCalendar(startTimeRes: Any, endTimeRes: Any?, firstWeekDay: Int = 1): List<DateBean> {
        // 生成日历
        val startCalendar = Calendar.getInstance()
        startCalendar.time = startDate.invoke(startTimeRes)
        val endCalendar = Calendar.getInstance()
        endCalendar.time = endDate.invoke(endTimeRes)
        return createDay(
            startCalendar = startCalendar,
            endCalendar = endCalendar,
            startArray = startArray.invoke(firstWeekDay),
            endArray = endArray.invoke(firstWeekDay)
        )
    }

    /**
     * 生成日
     * @param startCalendar 开始日期
     * @param endCalendar 结束日期
     */
    private fun createDay(
        startCalendar: Calendar,
        endCalendar: Calendar,
        startArray: Array<Int>,
        endArray: Array<Int>,
    ): List<DateBean> {
        val dateBeans = ArrayList<DateBean>()
        if (startCalendar.get(Calendar.DAY_OF_MONTH) != 1) {
            // 添加一次月份
            addMonth(
                dateBeans,
                (startCalendar.get(Calendar.MONTH) + 1).toString(),
                (startCalendar.get(Calendar.YEAR)).toString()
            )
            when (startCalendar.get(Calendar.DAY_OF_WEEK)) {
                1 -> { // 周日
                    addNullDay(dateBeans, startArray[0])
                }
                2 -> { // 周1
                    addNullDay(dateBeans, startArray[1])
                }
                3 -> { // 周2
                    addNullDay(dateBeans, startArray[2])
                }
                4 -> { // 周3
                    addNullDay(dateBeans, startArray[3])
                }
                5 -> { // 周4
                    addNullDay(dateBeans, startArray[4])
                }
                6 -> { // 周5
                    addNullDay(dateBeans, startArray[5])
                }
                7 -> { // 周6
                    addNullDay(dateBeans, startArray[6])
                }
            }
        }
        while (startCalendar.timeInMillis <= endCalendar.timeInMillis) {
            // 获取这个月的第一天
            if (startCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
                // 添加一次月份
                addMonth(
                    dateBeans,
                    (startCalendar.get(Calendar.MONTH) + 1).toString(),
                    (startCalendar.get(Calendar.YEAR)).toString()
                )
                // 判断是周几
                when (startCalendar.get(Calendar.DAY_OF_WEEK)) {
                    1 -> { // 周日
                        addNullDay(dateBeans, startArray[0])
                    }
                    2 -> { // 周1
                        addNullDay(dateBeans, startArray[1])
                    }
                    3 -> { // 周2
                        addNullDay(dateBeans, startArray[2])
                    }
                    4 -> { // 周3
                        addNullDay(dateBeans, startArray[3])
                    }
                    5 -> { // 周4
                        addNullDay(dateBeans, startArray[4])
                    }
                    6 -> { // 周5
                        addNullDay(dateBeans, startArray[5])
                    }
                    7 -> { // 周6
                        addNullDay(dateBeans, startArray[6])
                    }
                    else -> {
                        Log.e("日历", "末日")
                        addNullDay(dateBeans, 7)
                    }
                }
            }

            // 月份中间
            val dateBean = DateBean()
            dateBean.date = startCalendar.time
            dateBean.day = startCalendar.get(Calendar.DAY_OF_MONTH).toString()
            dateBean.month = (startCalendar.get(Calendar.MONTH) + 1).toString()
            dateBean.year = startCalendar.get(Calendar.YEAR).toString()
            dateBean.itemType = DateBean.ITEM_TYPE_DAY
            dateBean.itemState = DateBean.ITEM_STATE_NORMAL
            dateBeans.add(dateBean)

            // 获取当月最后一天
            if (startCalendar.get(Calendar.DAY_OF_MONTH) == startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                // 判断是周几
                when (startCalendar.get(Calendar.DAY_OF_WEEK)) {
                    1 -> { // 周日
                        addNullDay(dateBeans, endArray[0])
                    }
                    2 -> { // 周1
                        addNullDay(dateBeans, endArray[1])
                    }
                    3 -> { // 周2
                        addNullDay(dateBeans, endArray[2])
                    }
                    4 -> { // 周3
                        addNullDay(dateBeans, endArray[3])
                    }
                    5 -> { // 周4
                        addNullDay(dateBeans, endArray[4])
                    }
                    6 -> { // 周5
                        addNullDay(dateBeans, endArray[5])
                    }
                    7 -> { // 周6
                        addNullDay(dateBeans, endArray[6])
                    }
                }
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dateBeans
    }

    /**
     * 添加空白占位
     * @param dates 日期集合
     * @param count 次数
     */
    private fun addNullDay(dates: ArrayList<DateBean>, count: Int) {
        for (index in 1..count) {
            val dateBean = DateBean()
            dateBean.itemState = DateBean.ITEM_STATE_NO_CHECK
            dates.add(dateBean)
        }
    }

    /**
     * 添加月份
     * @param dates 日期集合
     * @param month 月份
     * @param year 年
     */
    private fun addMonth(dates: ArrayList<DateBean>, month: String, year: String) {
        val dateBean = DateBean()
        dateBean.itemType = DateBean.ITEM_TYPE_MONTH
        dateBean.year = year
        dateBean.month = month
        dateBean.day = "${year}-${month}"
        dates.add(dateBean)
    }

    /**
     * 一周的日期
     * @param timeRes 当周的日期，日期格式 yyyy-MM-dd 或者 Date
     * @param firstWeekDay 周几开始
     * @return ArrayList<DateBean> 一周的数据
     */
    fun createWeekDay(timeRes: Any, firstWeekDay: Int): ArrayList<DateBean> {
        val dateBeans = ArrayList<DateBean>()
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.time = currentDate.invoke(timeRes)
        val dayWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayWeek == 1) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        if (firstWeekDay == 1) {
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.add(Calendar.DATE, calendar.firstDayOfWeek - day)
        } else {
            calendar.firstDayOfWeek = Calendar.SUNDAY
            calendar.add(Calendar.DATE, calendar.firstDayOfWeek - day)
        }
        val dateBean = DateBean()
        dateBean.date = calendar.time
        dateBean.day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        dateBean.year = calendar.get(Calendar.YEAR).toString()
        dateBean.itemState = DateBean.ITEM_STATE_NORMAL
        dateBeans.add(dateBean)
        for (index in 1..6) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val dateBeanTemp = DateBean()
            dateBeanTemp.date = calendar.time
            dateBeanTemp.month = (calendar.get(Calendar.MONTH) + 1).toString()
            dateBeanTemp.day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            dateBeanTemp.year = calendar.get(Calendar.YEAR).toString()
            dateBeanTemp.itemState = DateBean.ITEM_STATE_NORMAL
            dateBeans.add(dateBeanTemp)
        }
        return dateBeans
    }

    /**
     * 生成指定月份
     * @param timeRes Any
     * @param firstWeekDay Int
     * @param otherDays Boolean
     * @return ArrayList<DateBean>
     */
    fun createMonth(timeRes: Any, firstWeekDay: Int = 7, otherDays: Boolean): ArrayList<DateBean> {

        // 月初
        val beginMonth = Calendar.getInstance()
        beginMonth.time = currentDate.invoke(timeRes)
        beginMonth.set(beginMonth.get(Calendar.YEAR), beginMonth.get(Calendar.MONTH), 1)
        // 月末
        val endMonth = Calendar.getInstance()
        endMonth.time = currentDate.invoke(timeRes)
        endMonth.add(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - endMonth.get(Calendar.DAY_OF_MONTH))
        return createMonthDays(
            beginMonth = beginMonth,
            endMonth = endMonth,
            otherDays = otherDays,
            startArray = startArray.invoke(firstWeekDay),
            endArray = endArray.invoke(firstWeekDay)
        )

    }

    /**
     * 生成指定月份的日历
     * @param beginMonth Calendar
     * @param endMonth Calendar
     * @param otherDays Boolean
     * @param startArray Array<Int>
     * @param endArray Array<Int>
     * @return ArrayList<DateBean>
     */
    private fun createMonthDays(
        beginMonth: Calendar,
        endMonth: Calendar,
        otherDays: Boolean,
        startArray: Array<Int>,
        endArray: Array<Int>,
    ): ArrayList<DateBean> {

        val dateBeans = ArrayList<DateBean>()

        if (otherDays) { // 是否生成其他月份的日期
            // 月初判断是周几
            when (beginMonth.get(Calendar.DAY_OF_WEEK)) {
                1 -> { // 周日
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[0])
                }
                2 -> { // 周1
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[1])
                }
                3 -> { // 周2
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[2])
                }
                4 -> { // 周3
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[3])
                }
                5 -> { // 周4
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[4])
                }
                6 -> { // 周5
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[5])
                }
                7 -> { // 周6
                    beginMonth.add(Calendar.DAY_OF_MONTH, -startArray[6])
                }
                else -> {
                    Log.e("日历", "末日")
                }
            }

            // 月末判断是周几
            when (endMonth.get(Calendar.DAY_OF_WEEK)) {
                1 -> { // 周日
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[0])
                }
                2 -> { // 周1
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[1])
                }
                3 -> { // 周2
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[2])
                }
                4 -> { // 周3
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[3])
                }
                5 -> { // 周4
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[4])
                }
                6 -> { // 周5
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[5])
                }
                7 -> { // 周6
                    endMonth.add(Calendar.DAY_OF_MONTH, endArray[6])
                }
            }

            while (beginMonth.time <= endMonth.time) {
                // 这个月的数据
                val dateBean = DateBean()
                dateBean.date = beginMonth.time
                dateBean.day = beginMonth.get(Calendar.DAY_OF_MONTH).toString()
                dateBean.month = (beginMonth.get(Calendar.MONTH) + 1).toString()
                dateBean.year = beginMonth.get(Calendar.YEAR).toString()
                dateBean.itemType = DateBean.ITEM_TYPE_DAY
                dateBean.itemState = DateBean.ITEM_STATE_NORMAL
                dateBeans.add(dateBean)
                beginMonth.add(Calendar.DAY_OF_MONTH, 1)
            }

        } else {

            while (beginMonth.time < endMonth.time) {
                // 获取这个月的第一天
                if (beginMonth.get(Calendar.DAY_OF_MONTH) == 1) {
                    // 添加一次月份
                    addMonth(
                        dateBeans,
                        (beginMonth.get(Calendar.MONTH) + 1).toString(),
                        (beginMonth.get(Calendar.YEAR)).toString()
                    )
                    // 判断是周几
                    when (beginMonth.get(Calendar.DAY_OF_WEEK)) {
                        1 -> { // 周日
                            addNullDay(dateBeans, startArray[0])
                        }
                        2 -> { // 周1
                            addNullDay(dateBeans, startArray[1])
                        }
                        3 -> { // 周2
                            addNullDay(dateBeans, startArray[2])
                        }
                        4 -> { // 周3
                            addNullDay(dateBeans, startArray[3])
                        }
                        5 -> { // 周4
                            addNullDay(dateBeans, startArray[4])
                        }
                        6 -> { // 周5
                            addNullDay(dateBeans, startArray[5])
                        }
                        7 -> { // 周6
                            addNullDay(dateBeans, startArray[6])
                        }
                        else -> {
                            Log.e("日历", "末日")
                            addNullDay(dateBeans, 7)
                        }
                    }
                }

                // 这个月的数据
                val dateBean = DateBean()
                dateBean.date = beginMonth.time
                dateBean.day = beginMonth.get(Calendar.DAY_OF_MONTH).toString()
                dateBean.month = (beginMonth.get(Calendar.MONTH) + 1).toString()
                dateBean.year = beginMonth.get(Calendar.YEAR).toString()
                dateBean.itemType = DateBean.ITEM_TYPE_DAY
                dateBean.itemState = DateBean.ITEM_STATE_NORMAL
                dateBeans.add(dateBean)

                // 获取当月最后一天
                if (beginMonth.get(Calendar.DAY_OF_MONTH) == beginMonth.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    // 判断是周几
                    when (beginMonth.get(Calendar.DAY_OF_WEEK)) {
                        1 -> { // 周日
                            addNullDay(dateBeans, endArray[0])
                        }
                        2 -> { // 周1
                            addNullDay(dateBeans, endArray[1])
                        }
                        3 -> { // 周2
                            addNullDay(dateBeans, endArray[2])
                        }
                        4 -> { // 周3
                            addNullDay(dateBeans, endArray[3])
                        }
                        5 -> { // 周4
                            addNullDay(dateBeans, endArray[4])
                        }
                        6 -> { // 周5
                            addNullDay(dateBeans, endArray[5])
                        }
                        7 -> { // 周6
                            addNullDay(dateBeans, endArray[6])
                        }
                    }
                }
                beginMonth.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return dateBeans
    }

}