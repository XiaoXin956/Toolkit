package com.xiaoxin.network.retrofit.bean

import com.google.gson.Gson

/**
 * @author: Admin
 * @date: 2021-12-02
 */
open class BaseHttpResponse<T> {

    var code: Int? = -1
    var msg: String = ""
    var success: Boolean? = null
    var value: T? = null

    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(code: Int, msg: String, value: T?) {
        this.code = code
        this.msg = msg
        this.value = value
    }

    constructor(code: Int, success: Boolean, msg: String, value: T?) {
        this.code = code
        this.msg = msg
        this.value = value
        this.success = success
    }

    constructor()

    override fun toString(): String {
        return Gson().toJson(this)
    }
}