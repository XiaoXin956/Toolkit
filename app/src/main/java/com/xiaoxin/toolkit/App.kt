package com.xiaoxin.toolkit

import android.app.Application
import com.xiaoxin.network.NetConfig

/**
 * @author: Admin
 * @date: 2022-03-24
 */
class App :Application() {

    companion object{
        var token:String = ""
    }

    override fun onCreate() {
        super.onCreate()
        NetConfig.printLog = true
    }

}