package com.xiaoxin.basic.date

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxin.basic.R
import com.xiaoxin.basic.databinding.CalendarViewBinding
import com.xiaoxin.basic.databinding.ItemCalenderBinding
import com.xiaoxin.basic.databinding.ItemWeekBinding
import com.xiaoxin.basic.mvvm.BaseBinRecycleView
import com.xiaoxin.basic.mvvm.BaseViewHolder
import com.xiaoxin.basic.view.ViewUtils


/**
 * @author: Admin
 * @date: 2022-04-15
 */
class CalendarView : FrameLayout {

    private lateinit var calendarView: CalendarViewBinding
    private var rvWeek: RecyclerView? = null
    private var rvCalendar: RecyclerView? = null
    private var dateBean: DateBean? = null
    private var isSingle = true
    private var oldPosition = -1
    private var weekAdapter: WeekAdapter? = null
    private var calendarAdapter: CalendarAdapter = CalendarAdapter()
    private var createCalendar: List<DateBean> = ArrayList()

    var isShowWeek = true  // 是否显示周
    var isShowMonth = true  // 是否显示周
    private var firstWeek = 7 // 默认一周的第一天是周日还是周一，默认周日

    private var startTime: String? = null
    private var endTime: String? = null

    private var selectStartDate: DateBean? = null
    private var selectEndDate: DateBean? = null
    private var itemSelectClick: ItemSelectClick? = null

    private lateinit var dateStart: Any
    private var dateEnd: Any? = null

    // 行高
    private var itemLineHeight: Float = ViewUtils.dp2px(5)

    //禁选文本大小
    private var textSize = ViewUtils.sp2px(16)

    //禁选文本颜色
    var banSelectTextColor = Color.WHITE

    //选中文本颜色
    var selectTextColor = Color.DKGRAY

    //未选中文本颜色
    var unSelectTextColor = Color.DKGRAY

    // 年月文本颜色
    var yearMonthTextColor = Color.DKGRAY

    //禁选文本背景
    var banSelectBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_un_select_background, null)

    //选中文本背景
    var selectSingleBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_select_single_background, null)

    //选中文本背景
    var selectBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_select_background, null)

    //未选中背景
    var unSelectBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_un_select_background, null)

    //选中开始文本背景
    var selectStartBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_select_start_background, null)

    //选中结束文本背景
    var selectEndBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_select_end_background, null)

    //选中结束文本背景
    var banSelectEndBackground: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.item_calender_ban_select_background, null)

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

    fun init(attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        isShowWeek = typedArray.getBoolean(R.styleable.CalendarView_isShowWeek, true)
        isSingle = typedArray.getBoolean(R.styleable.CalendarView_isSingle, true)
        startTime = typedArray.getString(R.styleable.CalendarView_startTime)
        endTime = typedArray.getString(R.styleable.CalendarView_endTime)
        firstWeek = typedArray.getInt(R.styleable.CalendarView_firstWeek, 7)
        yearMonthTextColor = typedArray.getColor(
            R.styleable.CalendarView_yearMonthTextColor,
            ResourcesCompat.getColor(resources, R.color.calender_ban_select_text_color, null)
        )
        banSelectTextColor = typedArray.getColor(
            R.styleable.CalendarView_banSelectTextColor,
            ResourcesCompat.getColor(resources, R.color.calender_ban_select_text_color, null)
        )
        selectTextColor = typedArray.getColor(
            R.styleable.CalendarView_selectTextColor,
            ResourcesCompat.getColor(resources, R.color.calender_select_text_color, null)
        )
        unSelectTextColor = typedArray.getColor(
            R.styleable.CalendarView_unSelectTextColor,
            ResourcesCompat.getColor(resources, R.color.calender_un_select_text_color, null)
        )
        itemLineHeight = typedArray.getDimension(R.styleable.CalendarView_itemLineHeight, 5F)
        // 背景/颜色
        typedArray.getDrawable(R.styleable.CalendarView_selectSingleBackground)?.let {
            selectSingleBackground = it
        }
        typedArray.getDrawable(R.styleable.CalendarView_selectBackground)?.let {
            selectBackground = it
        }
        typedArray.getDrawable(R.styleable.CalendarView_unSelectBackground)?.let {
            unSelectBackground = it
        }
        typedArray.getDrawable(R.styleable.CalendarView_selectStartBackground)?.let {
            selectStartBackground = it
        }
        typedArray.getDrawable(R.styleable.CalendarView_selectEndBackground)?.let {
            selectEndBackground = it
        }
        typedArray.getDrawable(R.styleable.CalendarView_selectEndBackground)?.let {
            banSelectEndBackground = it
        }
        typedArray.getDrawable(R.styleable.CalendarView_banSelectBackground)?.let {
            banSelectBackground = it
        }
        typedArray.getString(R.styleable.CalendarView_dateStart)?.let {
            dateStart = it
        }
        typedArray.getString(R.styleable.CalendarView_dateEnd)?.let {
            dateEnd = it
        }
        textSize = typedArray.getDimension(R.styleable.CalendarView_calendarTextSize, 16F)
        if (isSingle) {
            selectBackground = selectSingleBackground
        }
        typedArray.recycle()
    }

    /**
     * 配置时间
     * @param dateStart 开始时间
     * @param dateEnd 结束时间，如果为空，则为当前时间
     */
    fun getCalendarDate(dateStart: Any, dateEnd: Any?, isCurrentMonth: Boolean? = false) {
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        calendarData(dateStart, dateEnd, isCurrentMonth)
    }


    fun build() {
        calendarView = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.calendar_view,
            this,
            false
        )
        addView(calendarView.root)
        if (isShowWeek) {
            weekData()
        }
        rvCalendar = calendarView.rvCalendar
        val gridLayoutManager = GridLayoutManager(context, 7)
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(i: Int): Int {
                return if (DateBean.ITEM_TYPE_MONTH == calendarAdapter.getData(i).itemType) {
                    7
                } else {
//                    if (isShowMonth) {
                        1
//                    } else {
//                        0
//                    }
                }
            }
        }
        rvCalendar?.addItemDecoration(SpacesItemDecoration(itemLineHeight))
        rvCalendar?.layoutManager = gridLayoutManager
        calendarAdapter.refresh(createCalendar)
        calendarAdapter.setItemListener { _, data, position ->
            if (itemSelectClick == null) {
                return@setItemListener
            }
            if (data.itemType == DateBean.ITEM_TYPE_MONTH || data.itemState == DateBean.ITEM_STATE_NO_CHECK) return@setItemListener
            if (isSingle) {
                if (oldPosition == -1) {
                    oldPosition = position
                }
                if (data.itemState == DateBean.ITEM_STATE_NO_CHECK) return@setItemListener
                if (dateBean == null) {
                    dateBean = data
                }
                dateBean?.itemState = DateBean.ITEM_STATE_NORMAL
                calendarAdapter.notifyItemChanged(oldPosition)
                oldPosition = position
                dateBean = data
                dateBean?.itemState = DateBean.ITEM_STATE_SELECTED
                calendarAdapter.notifyItemChanged(position)
                itemSelectClick?.selectItem?.invoke(dateBean!!)
            } else {
                // 多选
                if (selectStartDate == null) {
                    selectStartDate = data
                    data.itemState = DateBean.ITEM_STATE_BEGIN_DATE
                    calendarAdapter.notifyItemChanged(calendarAdapter.getDataIndexOf(selectStartDate))
                    itemSelectClick?.selectStartItem?.invoke(selectStartDate!!)
                } else if (selectEndDate == null) {
                    // 判断开始日期与结束日期是否一致
                    when {
                        selectStartDate == selectEndDate -> {
                            selectStartDate = data
                            selectEndDate = data
                            itemSelectClick?.selectStartItem?.invoke(selectStartDate!!)
                            calendarAdapter.notifyItemChanged(
                                calendarAdapter.getDataIndexOf(
                                    selectStartDate
                                )
                            )
                        }
                        data.date!!.time < selectStartDate!!.date!!.time -> {
                            //判断结束日期是否大于开始日期
                            selectStartDate?.itemState = DateBean.ITEM_STATE_NORMAL
                            calendarAdapter.notifyItemChanged(
                                calendarAdapter.getDataIndexOf(
                                    selectStartDate
                                )
                            )
                            selectStartDate = data
                            selectStartDate?.itemState = DateBean.ITEM_STATE_BEGIN_DATE
                            calendarAdapter.notifyItemChanged(
                                calendarAdapter.getDataIndexOf(
                                    selectStartDate
                                )
                            )
                            itemSelectClick?.selectStartItem?.invoke(selectStartDate!!)
                        }
                        else -> {
                            // 选中结束日期
                            selectEndDate = data
                            selectEndDate?.itemState = DateBean.ITEM_STATE_END_DATE
                            setState()
                            calendarAdapter.notifyItemChanged(position)
                            itemSelectClick?.selectEndItem?.invoke(selectEndDate!!)
                            itemSelectClick?.selectRangeItem?.invoke(
                                selectStartDate!!,
                                selectEndDate!!
                            )
                        }
                    }
                } else if (selectStartDate != null && selectEndDate != null) {
                    clearState()
                    selectEndDate?.itemState = DateBean.ITEM_STATE_NORMAL
                    selectEndDate = null
                    selectStartDate?.itemState = DateBean.ITEM_STATE_NORMAL
                    selectStartDate = data
                    selectStartDate?.itemState = DateBean.ITEM_STATE_BEGIN_DATE
                    calendarAdapter.notifyItemChanged(position)
                    itemSelectClick?.selectStartItem?.invoke(selectStartDate!!)
                }
            }
        }
        rvCalendar?.adapter = calendarAdapter

//        calendarData(dateStart, dateEnd, isCurrentMonth = false)
    }

    /**
     * 修改选中的日期部分
     */
    private fun setState() {
        if (selectStartDate != null && selectEndDate != null) {
            var start: Int = calendarAdapter.getData().indexOf(selectStartDate!!)
            val tempStart = start
            start += 1
            val end = calendarAdapter.getData().indexOf(selectEndDate!!)
            while (start < end) {
                val data = calendarAdapter.getData(start)
                if (data.itemType == DateBean.ITEM_TYPE_DAY) {
                    if (data.itemState != DateBean.ITEM_STATE_NO_CHECK) {
                        data.itemState = DateBean.ITEM_STATE_SELECTED
                    }
                }
                start += 1
            }
            calendarAdapter.notifyItemRangeChanged(tempStart, end)
        }
    }

    /**
     * 取消选中状态
     */
    private fun clearState() {
        if (selectEndDate != null && selectStartDate != null) {
            var start: Int = calendarAdapter.getData().indexOf(selectStartDate)
            val tempStart = start
            start += 1
            val end: Int = calendarAdapter.getData().indexOf(selectEndDate)
            while (start < end) {
                val dateBean: DateBean = calendarAdapter.getData(start)
                if (dateBean.itemType == DateBean.ITEM_TYPE_DAY &&
                    dateBean.itemState != DateBean.ITEM_STATE_NO_CHECK
                ) {
                    dateBean.itemState = DateBean.ITEM_STATE_NORMAL
                }
                start += 1
            }
            calendarAdapter.notifyItemRangeChanged(tempStart, end)
        }
    }

    /**
     * 周数据
     */
    private fun weekData() {
        rvWeek = calendarView.rvWeek
        rvWeek?.layoutManager = GridLayoutManager(context, 7)
        weekAdapter = WeekAdapter()
        rvWeek?.adapter = weekAdapter
        val weekList = if (firstWeek == 1) {
            resources.getStringArray(R.array.MondayWeek)
        } else {
            resources.getStringArray(R.array.SundayWeek)
        }
        val toList = weekList.toList()
        weekAdapter?.refresh(toList)
    }

    /**
     * 日历数据
     * @param startDate Any
     * @param endDate Any?
     */
    private fun calendarData(startDate: Any, endDate: Any?, isCurrentMonth: Boolean? = false) {
        createCalendar = if (isCurrentMonth!!) {
            CalendarUtils().createMonth(
                timeRes = startDate,
                firstWeekDay = firstWeek,
                otherDays = true
            )
        } else {
            CalendarUtils().createCalendar(
                startTimeRes = startDate,
                endTimeRes = endDate,
                firstWeekDay = firstWeek
            )
        }
        calendarAdapter.refresh(createCalendar)
    }

    /**
     * 周适配器
     * @property layoutId Int
     */
    class WeekAdapter : BaseBinRecycleView<String, ItemWeekBinding>() {

        override val layoutId: Int
            get() = R.layout.item_week

        override fun onBinData(
            holder: BaseViewHolder<ItemWeekBinding>,
            position: Int,
            data: String
        ) {
            holder.binding?.week = data
        }

    }

    /**
     * 分割线
     * @property height 边距的宽度
     * @constructor
     */
    class SpacesItemDecoration(private val height: Float = 1f) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (parent.layoutManager is GridLayoutManager) {
                outRect.left = (height).toInt()
                outRect.right = (height).toInt()
                outRect.top = (height).toInt()
                outRect.bottom = (height).toInt()
            }
        }

    }

    /**
     * 日历适配器
     * @property layoutId Int
     */
    inner class CalendarAdapter : BaseBinRecycleView<DateBean, ItemCalenderBinding>() {

        override val layoutId: Int
            get() = R.layout.item_calender

        override fun onBinData(
            holder: BaseViewHolder<ItemCalenderBinding>,
            position: Int,
            data: DateBean
        ) {
            holder.binding?.dateBean = data
            val tvDay = holder.binding?.tvDay!!
            tvDay.textSize = textSize
            if (data.itemType == DateBean.ITEM_TYPE_MONTH) {
                tvDay.setTextColor(yearMonthTextColor)
                return
            }
            when (data.itemState) {
                DateBean.ITEM_STATE_NORMAL -> {
                    tvDay.setTextColor(unSelectTextColor)
                    tvDay.background = unSelectBackground
                }
                DateBean.ITEM_STATE_SELECTED -> {
                    tvDay.setTextColor(selectTextColor)
                    tvDay.background = selectBackground
                }
                DateBean.ITEM_STATE_NO_CHECK -> {
                    tvDay.setTextColor(Color.WHITE)
                    tvDay.background = banSelectBackground
                }
                DateBean.ITEM_STATE_BEGIN_DATE -> {
                    tvDay.setTextColor(selectTextColor)
                    tvDay.background = selectStartBackground
                }
                DateBean.ITEM_STATE_END_DATE -> {
                    tvDay.setTextColor(selectTextColor)
                    tvDay.background = selectEndBackground
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return getData(position = position).itemType
        }

    }

    fun addItemSelectClickListener(method: ItemSelectClick) {
        itemSelectClick = method
    }

}

/**
 * 日期的事件监听
 * @receiver CalendarView 日历 View
 * @param init [@kotlin.ExtensionFunctionType] Function1<ItemSelectClick, Unit>
 */
inline fun CalendarView.onItemSelectDate(init: ItemSelectClick.() -> Unit) {
    val itemClick = ItemSelectClick()
    itemClick.init()
    addItemSelectClickListener(method = itemClick)
}

class ItemSelectClick {

    /**
     * 选中时间（单选使用）
     * @param ((selectDateBean: DateBean?) -> Unit) 选中时间
     */
    var selectItem: ((selectDateBean: DateBean?) -> Unit)? = null

    /**
     * 开始时间
     * @param ((selectStartDateBean: DateBean?) -> Unit) 开始时间
     */
    var selectStartItem: ((selectStartDateBean: DateBean?) -> Unit)? = null

    /**
     * 结束时间
     * @param ((selectEndDateBean: DateBean?) -> Unit) 结束时间
     */
    var selectEndItem: ((selectEndDateBean: DateBean?) -> Unit)? = null

    /**
     * 选中的范围
     * @param ((beginDateBean: DateBean?, endDateBean: DateBean?) -> Unit) 选中的时间范围
     */
    var selectRangeItem: ((beginDateBean: DateBean?, endDateBean: DateBean?) -> Unit)? = null

    fun onSelectItem(method: ((selectDateBean: DateBean?) -> Unit)) {
        selectItem = method
    }

    fun onSelectStartItem(method: ((selectStartDateBean: DateBean?) -> Unit)) {
        selectStartItem = method
    }

    fun onSelectEndItem(method: ((selectEndDateBean: DateBean?) -> Unit)) {
        selectEndItem = method
    }

    fun onSelectRangeItem(method: ((beginDateBean: DateBean?, endDateBean: DateBean?) -> Unit)) {
        selectRangeItem = method
    }


}


