package com.xiaoxin.toolkit

import android.app.Application
import com.xiaoxin.basic.base.AppFrontBackHelper
import com.xiaoxin.basic.toast.ToastUtils
import com.xiaoxin.network.retrofit.LogConfig

/**
 * @author: Admin
 * @date: 2022-03-24
 */
class App : Application() {

    companion object {
        var token: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        LogConfig.printLog = false
        ToastUtils.init(this)
        val appFrontBackHelper = AppFrontBackHelper()
        appFrontBackHelper.register(this,
            onFront = {
                      ToastUtils.showShort("当前在前台")
            },
            onBack = {
                ToastUtils.showShort("当前在后台")
            }
        )
    }

}