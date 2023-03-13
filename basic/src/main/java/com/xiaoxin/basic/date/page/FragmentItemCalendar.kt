package com.xiaoxin.basic.date.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.xiaoxin.basic.R
import com.xiaoxin.basic.databinding.FragmentCalendarBinding
import com.xiaoxin.basic.date.CalendarView


const val SHOW_DATE:String = "date"


/**
 * @author: Admin
 * @date: 2022-04-21
 */
class FragmentItemCalendar : Fragment() {

    // 显示的时间
    private lateinit var showDate:String
    private lateinit var fragmentCalendarBinding: FragmentCalendarBinding
    var calendarView: CalendarView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showDate = it.getString(SHOW_DATE).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCalendarBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_calendar, container, false)

        calendarView = fragmentCalendarBinding.calendarDay
        calendarView?.let {
            it.getCalendarDate(dateStart = showDate,dateEnd = null,isCurrentMonth = true)
            it.build()
        }
        return fragmentCalendarBinding.root
    }


}