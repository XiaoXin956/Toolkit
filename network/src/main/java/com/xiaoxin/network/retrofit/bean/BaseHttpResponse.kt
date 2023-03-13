package com.xiaoxin.network.retrofit.bean

import com.google.gson.Gson

open class BaseHttpResponse<T> {

    var code:Int?=-1

    var msg:String=""

    var success:Boolean?=null

    var value:T?=null

    constructor()

    constructor(code: Int?, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(code: Int?, msg: String, value: T?) {
        this.code = code
        this.msg = msg
        this.value = value
    }

    constructor(code: Int?, msg: String, success: Boolean?, value: T?) {
        this.code = code
        this.msg = msg
        this.success = success
        this.value = value
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }


}