package com.xiaoxin.basic.utils

import android.util.Log

fun log(tag: String = "日志", level: String = "e", msg: String) {

    when(level){
        "d"->{
            Log.d(tag, msg)
        }
        "e"->{
            Log.e(tag, msg)
        }

    }

}