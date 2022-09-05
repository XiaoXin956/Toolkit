package com.xiaoxin.toolkit

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.xiaoxin.basic.date.CalendarUtils
import com.xiaoxin.basic.date.CalendarView
import com.xiaoxin.basic.date.onItemSelectDate
import com.xiaoxin.basic.dialog.BaseDialog
import com.xiaoxin.basic.toast.ToastUtils
import com.xiaoxin.basic.utils.log
import com.xiaoxin.common.widget.LetterSideBar
import com.xiaoxin.common.widget.addTextChangeListener
import com.xiaoxin.common.widget.click
import com.xiaoxin.network.retrofit.LogConfig
import com.xiaoxin.toolkit.databinding.ActivityMainBinding
import com.xiaoxin.toolkit.viewmodel.UserViewModel
import java.io.File

class MainActivity : AppCompatActivity() {

    var activityMainBinding: ActivityMainBinding? = null
    var editMain: EditText? = null
    var tab: TabLayout? = null
    var btn_1: Button? = null
    var btn_2: Button? = null
    var btn_compose: Button? = null
    var btnScreenRecording: Button? = null
    var btnAddressCode: Button? = null
    var btn_up_load: Button? = null
    var btn_down_load: Button? = null
    var btn_page_calender: Button? = null
    var editName: EditText? = null
    var editPwd: EditText? = null
    var editFilePath: EditText? = null

    lateinit var load: StepLoadView
    lateinit var lsZim: LetterSideBar

    lateinit var cvCalender: CalendarView
    lateinit var context: Context

    private val userViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding!!.userViewModel = userViewModel
        activityMainBinding!!.lifecycleOwner = this

        context = this

        btnAddressCode = activityMainBinding!!.btnScreenRecordingStop
        btnScreenRecording = activityMainBinding!!.btnScreenRecording
        load = activityMainBinding!!.load
        lsZim = activityMainBinding!!.lsZim
        val arr = arrayOf("一", "咦", "亿")
        lsZim.setLetters(arr)

        LogConfig.printLog = false

        editMain = findViewById(R.id.edit_main)
        editMain?.addTextChangeListener {

            onTextChanged { charSequence, i, i2, i3 ->
                userViewModel.searchData(charSequence.toString())

                Log.e("扩展名", fileType(charSequence.toString()))

            }

//            onAfterTextChanged { s, _, _, _ ->
//                userViewModel.resText.value = s.toString()
//            }
        }
        tab = findViewById(R.id.tab)
        editName = findViewById(R.id.edit_name)
        editPwd = findViewById(R.id.edit_pwd)
        editFilePath = findViewById(R.id.edit_file_path)

        btn_1 = findViewById(R.id.btn_1)
        btn_1?.setOnClickListener {
            val map = HashMap<String, String>()
            map["username"] = editName?.text.toString()
            map["password"] = editPwd?.text.toString()
            userViewModel.login(map)
            btn_1?.isEnabled = false

        }

        btn_2 = findViewById(R.id.btn_2)
        btn_2?.setOnClickListener {
            val map = HashMap<String, String>()
            map["username"] = editName?.text.toString()
            map["password"] = editPwd?.text.toString()
            userViewModel.login3(map)
            btn_2?.isEnabled = false

        }

        btn_compose = findViewById(R.id.btn_compose)
        btn_compose?.setOnClickListener {

//            var intent = Intent(this, HitokotoActivity::class.java)
//            startActivity(intent)
        }

        btn_up_load = findViewById(R.id.btn_up_load)
        btn_up_load?.setOnClickListener {
            val map = HashMap<String, Any>()
            val file = File(editFilePath!!.text.toString().trim())
            map["upfile"] = file
            userViewModel.upLoadFile(map)
        }

        btn_up_load = findViewById(R.id.btn_up_load)
        btn_up_load?.setOnClickListener {
            val map = HashMap<String, Any>()
            val file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1647960986389.jpg")
//            var file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1645365885424.jpg")
            map["upfile"] = file
            userViewModel.upLoadFile(map)
        }

        btn_down_load = findViewById(R.id.btn_down_load)
        btn_down_load?.click {
            onClick {
                val map = HashMap<String, Any>()
                val file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1647960986389.jpg")
//            var file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1645365885424.jpg")
                map["upfile"] = file
                userViewModel.downLoadFile()
            }
            onLongClick {
                ToastUtils.showShort("弹框测试")
            }
        }

        userViewModel.resLogin.observe(this) {
//            editMain?.setText(it)
            btn_1?.isEnabled = true
            btn_2?.isEnabled = true

        }

        val anmin = ValueAnimator.ofFloat(0f, 5000f)
        anmin.addUpdateListener {
            val value: Int = it.animatedValue.toString().toFloat().toInt()
            load.setCurrentStep(value)
        }
        anmin.repeatCount = 5
        anmin.duration = 3000
        anmin.start()

        lsZim.setTouchListener { res, index, b ->
            ToastUtils.showShort("${res}")
            Log.e("字母", "${res}")
        }


        cvCalender = activityMainBinding!!.cvCal
        cvCalender.getCalendarDate("2021-09-01", null)
        cvCalender.build()
        cvCalender.onItemSelectDate {
            onSelectItem {
                Log.e("日历", "选中日期：${it.toString()}")
            }
            onSelectStartItem {
                Log.e("日历", "开始日期：${it.toString()}")
            }
            onSelectEndItem {
                Log.e("日历", "结束日期：${it.toString()}")
            }
            onSelectRangeItem { beginDateBean, endDateBean ->
                Log.e("日历", "选中范围：${beginDateBean.toString()}  至  ${endDateBean.toString()}")
            }
        }


        btn_page_calender = findViewById(R.id.btn_page_calender)
        btn_page_calender?.setOnClickListener {
            startActivity(Intent(this, PageSlideActivity::class.java))

        }

        val createWeekDay =
            CalendarUtils().createWeekDay("2022-04-20", CalendarUtils.FIRST_DAY_MONDAY)
//        createWeekDay.forEach {
//            Log.e("日历", "日期${it.year} ${it.month} ${it.day} ")
//        }

//        val createMonthDay = CalendarUtils().createMonth("2022-03-20", CalendarUtils.FIRST_DAY_MONDAY,false)
//        val sb1 = StringBuffer()
//        createMonthDay.forEach {
//            sb1.append("${it.year} ${it.month} ${it.day}   ")
//        }
//        Log.e("日历", sb1.toString())
//        val createMonthDay2 = CalendarUtils().createMonth("2022-01-20", CalendarUtils.FIRST_DAY_MONDAY,false)
//        val sb2 = StringBuffer()
//        createMonthDay2.forEach {
//            sb2.append("${it.year} ${it.month} ${it.day}   ")
//        }
//        Log.e("日历", sb2.toString())

        val createMonthDay3 =
            CalendarUtils().createMonth("2022-03-20", CalendarUtils.FIRST_DAY_MONDAY, true)
        val sb3 = StringBuffer()
        createMonthDay3.forEach {
            sb3.append("${it.year}-${it.month}-${it.day}   ")
        }
        Log.e("日历", sb3.toString())
        val createMonthDay4 =
            CalendarUtils().createMonth("2022-03-20", CalendarUtils.FIRST_DAY_MONDAY, false)
        val sb4 = StringBuffer()
        createMonthDay4.forEach {
            sb4.append("${it.year}-${it.month}-${it.day}   ")
        }
        Log.e("日历", sb4.toString())

        calendarData("2021-10-21", null, true)



        activityMainBinding?.btnPay?.click {
            onClick {

            }
        }


        btnAddressCode?.click {
            onClick {

                var maps = HashMap<String,Any>()
                maps["address"] = "广东深圳龙华"

                userViewModel.reqAddressCode(maps)
            }
        }

        userViewModel.addressCode.observe(this){

            log(tag = "位置", msg = it.toString())

        }

    }


    /**
     * 日历数据
     * @param startDate Any
     * @param endDate Any?
     */
    private fun calendarData(startDate: Any, endDate: Any?, isCurrentMonth: Boolean? = false) {
        val createMonth = if (isCurrentMonth!!) {
            CalendarUtils().createMonth(
                timeRes = startDate,
                firstWeekDay = 7,
                otherDays = true
            )
        } else {
            CalendarUtils().createCalendar(
                startTimeRes = startDate,
                endTimeRes = endDate,
                firstWeekDay = 7
            )
        }
        val sb5 = StringBuffer()
        createMonth.forEach {
            sb5.append("${it.year}-${it.month}-${it.day}   ")
        }
        Log.e("日历", sb5.toString())
    }


    private fun fileType(filePath: String): String {
        var type = "application/json"
        val lastIndexOf = filePath.lastIndexOf(".")
        if (lastIndexOf == -1) {
            return ""
        }
        val i = filePath.length
        val extensionType = filePath.substring(lastIndexOf + 1, i)
        if (extensionType == "jpg" || extensionType == "jpeg" || extensionType == "jpe") {
            type = "image/jpeg"
        } else if (extensionType == "png") {
            type = "image/png"
        } else if (extensionType == "svg") {
            type = "image/svg+xml"
        } else if (extensionType == "gif") {
            type = "image/gif"
        } else if (extensionType == "mp3") {
            type = "audio/mpeg"
        } else if (extensionType == "avi") {
            type = "video/x-msvideo"
        } else if (extensionType == "xml") {
            type = "application/xml"
        } else if (extensionType == "js") {
            type = "application/x-javascript"
        } else if (extensionType == "ppt") {
            type = "application/vnd.ms-powerpoint"
        } else if (extensionType == "txt") {
            type = "text/plain"
        } else if (extensionType == "zip") {
            type = "application/zip"
        }
        return type
    }

}