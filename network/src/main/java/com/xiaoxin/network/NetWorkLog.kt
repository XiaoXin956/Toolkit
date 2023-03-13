package com.xiaoxin.network

import android.util.Log

object NetWorkLog {

    fun print(msg: String) {
        kotlin.io.print("\n\n" + msg + "\n\n")
    }

    fun log(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun log(msg: String) {
        Log.d("NetWork", msg)
    }

}

