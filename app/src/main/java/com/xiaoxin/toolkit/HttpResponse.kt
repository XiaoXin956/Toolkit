package com.xiaoxin.toolkit

import com.google.gson.Gson
import com.xiaoxin.network.retrofit.bean.BaseHttpResponse
import java.io.Serializable

/**
 * @author: Admin
 * @date: 2021-12-02
 */
class HttpResponse<T> : BaseHttpResponse<T> {

    constructor(code: Int, msg: String) : super(code, msg)
    constructor(code: Int, msg: String, value: T?) : super(code, msg, value)
    constructor(code: Int, success: Boolean, msg: String, value: T?) : super(code,success,msg,value)

    override fun toString(): String {
        return Gson().toJson(this)
    }
}