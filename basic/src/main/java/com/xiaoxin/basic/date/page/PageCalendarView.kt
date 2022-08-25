package com.xiaoxin.basic.date.page

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xiaoxin.basic.R
import com.xiaoxin.basic.databinding.PageCalendarViewBinding
import com.xiaoxin.basic.date.DateToString
import com.xiaoxin.basic.date.FormatYYYYMMDD
import com.xiaoxin.basic.date.DateBean
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author: Admin
 * @date: 2022-04-20
 */
class PageCalendarView : FrameLayout {

    lateinit var vpPageCalendar: ViewPager2
    private var showDate: String? = null
    private lateinit var calendarFragments: ArrayList<FragmentItemCalendar>
    private lateinit var calendarBean: ArrayList<ViewPageBean>
    var fragmentItemCalendar: FragmentItemCalendar? = null
    var pageFragmentAdapter : PageFragmentAdapter? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)


    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        val pageCalendarView = DataBindingUtil.inflate<PageCalendarViewBinding>(
            LayoutInflater.from(context),
            R.layout.page_calendar_view,
            null,
            false
        )
        addView(pageCalendarView.root)
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.PageCalendarView)
        typedArray.getString(R.styleable.PageCalendarView_showDate)?.let {
            showDate = it
        }
        typedArray.recycle()

        calendarFragments = ArrayList()
        calendarBean = ArrayList()

        var tempStartDate = FormatYYYYMMDD.parse(showDate!!)!!
        val instance = Calendar.getInstance(Locale.CHINA)
        instance.time = tempStartDate
        instance.add(Calendar.MONTH, -3)
        for (index in 1..7) {
            tempStartDate = instance.time
            addCalendarView(DateToString(tempStartDate, FormatYYYYMMDD), null)
            instance.add(Calendar.MONTH, 1)
        }

        vpPageCalendar = pageCalendarView.vpPageCalendar
        vpPageCalendar.offscreenPageLimit = 7
        if (context is FragmentActivity) {
            pageFragmentAdapter = PageFragmentAdapter((context as FragmentActivity), calendarFragments)
            vpPageCalendar.adapter = pageFragmentAdapter
        }
        vpPageCalendar.setCurrentItem(3,false)
        vpPageCalendar.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            var currentPosition:Int = 3

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                currentPosition= position
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

//                if (currentPosition<3){
//                    addCalendarView("2022-05-01", 0)
//                    pageFragmentAdapter?.removeFragment(calendarFragments.size-1)
//                }else if (currentPosition>3){
//                    addCalendarView("2022-05-01", calendarFragments.size-1)
//                    pageFragmentAdapter?.removeFragment(0)
//                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

    }

    private fun addCalendarView(timeRes: Any, index: Int?) {

        if (index==null) {
            fragmentItemCalendar = FragmentItemCalendar()
            val bundle = Bundle()
            bundle.putString(SHOW_DATE, timeRes.toString())
            fragmentItemCalendar?.arguments = bundle
            calendarFragments.add(fragmentItemCalendar!!)
        }else{

            fragmentItemCalendar = FragmentItemCalendar()
            val bundle = Bundle()
            bundle.putString(SHOW_DATE, timeRes.toString())
            fragmentItemCalendar?.arguments = bundle
//            calendarFragments.add(index,fragmentItemCalendar!!)
//            pageFragmentAdapter?.notifyDataSetChanged()
//            pageFragmentAdapter?.addFragment(index,fragmentItemCalendar!!)

        }
    }

    inner class PageFragmentAdapter : FragmentStateAdapter {

        var fragments: ArrayList<FragmentItemCalendar>

        constructor(fragmentActivity: FragmentActivity, fragments: ArrayList<FragmentItemCalendar>) : super(fragmentActivity) {
            this.fragments = fragments
        }

        constructor(fragment: Fragment, fragments: ArrayList<FragmentItemCalendar>) : super(fragment) {
            this.fragments = fragments
        }

        constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, fragments: ArrayList<FragmentItemCalendar>) : super(fragmentManager, lifecycle) {
            this.fragments = fragments
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }

    class ViewPageBean(var view: View?, var dateBean: DateBean?)


}