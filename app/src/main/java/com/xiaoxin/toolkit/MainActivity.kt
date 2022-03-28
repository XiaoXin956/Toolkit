package com.xiaoxin.toolkit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.xiaoxin.network.NetConfig
import com.xiaoxin.toolkit.databinding.ActivityMainBinding
import com.xiaoxin.toolkit.viewmodel.UserViewModel
import java.io.File

class MainActivity : AppCompatActivity() {

    var activityMainBinding:ActivityMainBinding?=null
    var editMain: EditText? = null
    var tab: TabLayout? = null
    var btn_1: Button? = null
    var btn_up_load: Button? = null
    var btn_down_load: Button? = null
    var editName:EditText?=null
    var editPwd:EditText?=null

    private val userViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        activityMainBinding!!.userViewModel = userViewModel
        activityMainBinding!!.lifecycleOwner = this

        NetConfig.printLog = true

        editMain = findViewById(R.id.edit_main)
        editMain = findViewById(R.id.edit_main)
        editMain?.textChangedListener {
            onTextChangedListener { s, start, before, count ->
                userViewModel.resText.value = s.toString()
            }
        }
        tab = findViewById(R.id.tab)
        editName = findViewById(R.id.edit_name)
        editPwd = findViewById(R.id.edit_pwd)

        btn_1 = findViewById(R.id.btn_1)
        btn_1?.setOnClickListener {
            val map = HashMap<String, String>()
            map["username"] = editName?.text.toString()
            map["password"] = editPwd?.text.toString()
            userViewModel.login(map)
            btn_1?.isEnabled = false
        }

        btn_up_load = findViewById(R.id.btn_up_load)
        btn_up_load?.setOnClickListener {
            val map = HashMap<String,Any>()
            val file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1647960986389.jpg")
//            var file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1645365885424.jpg")
            map["upfile"] = file
            userViewModel.upLoadFile(map)
        }

        btn_up_load = findViewById(R.id.btn_up_load)
        btn_up_load?.setOnClickListener {
            val map = HashMap<String,Any>()
            val file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1647960986389.jpg")
//            var file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1645365885424.jpg")
            map["upfile"] = file
            userViewModel.upLoadFile(map)
        }

        btn_down_load = findViewById(R.id.btn_down_load)
        btn_down_load?.setOnClickListener {
            val map = HashMap<String,Any>()
            val file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1647960986389.jpg")
//            var file = File("/storage/emulated/0/Pictures/WeiXin/wx_camera_1645365885424.jpg")
            map["upfile"] = file
            userViewModel.downLoadFile()
        }
        userViewModel.resLogin.observe(this){
//            editMain?.setText(it)
            btn_1?.isEnabled = true
        }

    }

}