package com.xiaoxin.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xiaoxin.common.utils.ToastUtils

/**
 * @author: Admin
 * @date: 2021-08-23
 */
abstract class BasexFragment : Fragment(),View.OnClickListener {

    val tagF: String = javaClass.name
    lateinit var activity: Activity
    private var isLoader = false
    lateinit var application:Application

    open fun <V : View?> getView(activity: Activity, viewId: Int): V {
        return activity.findViewById(viewId)
    }

    open fun <T : View?> getView(view: View, viewId: Int): T {
        return view.findViewById(viewId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity()
        application = activity.application
    }

    override fun onResume() {
        super.onResume()
        if (!isLoader && !isHidden) {
            lazyInit()
            isLoader = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoader = false
    }

    /**
     * 日志打印
     */
    fun log(msg: String) {
        log(msg, "e")
    }
    fun log(@NonNull msg: String, flag: String) {
        when (flag) {
            "v" -> {
                Log.v(tag, msg);
            }
            "d" -> {
                Log.d(tag, msg);
            }
            "i" -> {
                Log.i(tag, msg);
            }
            "w" -> {
                Log.w(tag, msg);
            }
            "e" -> {
                Log.e(tag, msg);
            }
        }


    }

    /**
     * 显示提示弹框
     */
    fun showToast(@NonNull msg: String) {
        ToastUtils.showShort(context, msg);
    }

    abstract fun lazyInit();

     override fun onClick(v: View){
         
     }

}