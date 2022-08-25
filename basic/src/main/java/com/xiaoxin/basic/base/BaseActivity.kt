package com.xiaoxin.basic.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xiaoxin.basic.toast.ToastUtils
import com.xiaoxin.common.base.ActivityManagers

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    var tag: String? = null
    lateinit var context: Context
    lateinit var activity: AppCompatActivity
    lateinit var activityManagers: ActivityManagers

    fun setContentView(): Int {
        return -1
    }

    override fun onClick(v: View?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tag = localClassName
        context = this
        activity = this
        activityManagers = ActivityManagers().getActivityManager(context)!!
        activityManagers.putActivity(activity)

    }

    override fun onDestroy() {
        super.onDestroy()
        activityManagers.removeActivity(activity)
    }


    /**
     * 获取当前显示的fragment
     * @return Fragment?
     */
    fun getVisibleFragment(): Fragment? {
        val supportFragmentManager = activity.supportFragmentManager
        val fragments = supportFragmentManager.fragments
        var fragment: Fragment? = null
        for (fragmentTemp in fragments) {
            if (fragmentTemp != null && fragmentTemp.isVisible) {
                fragment =  fragmentTemp
                break
            }
        }
        return fragment
    }

    fun exit() {
        activityManagers.exit()
    }

    /**
     * 日志打印
     */
    fun log(@NonNull msg: String, flag: String) {
        when (flag) {
            "v" -> {
                Log.v(tag, msg)
            }
            "d" -> {
                Log.d(tag, msg)
            }
            "i" -> {
                Log.i(tag, msg)
            }
            "w" -> {
                Log.w(tag, msg)
            }
            "e" -> {
                Log.e(tag, msg)
            }
        }


    }

    /**
     * 显示提示弹框
     */
    fun showToast(@NonNull msg: String) {
        ToastUtils.showShort(msg)
    }

}