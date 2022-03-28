package com.xiaoxin.toolkit

import com.google.gson.Gson
import java.io.Serializable

/**
 * @author: Admin
 * @date: 2021-12-02
 */
class HttpResponse<T> {

    var code: Int? = -1
    var msg: String = ""
    var success: Boolean? = null
    var value: T? = null

    constructor()//:this(-1,false,"异常",null)
    constructor(code: Int, msg: String) {
        this.code =code
        this.msg =msg
    }
    constructor(code: Int, msg: String, value: T?) {
        this.code =code
        this.msg =msg
        this.value =value
    }//:this(code,false,msg,null)
    constructor(code: Int, success: Boolean, msg: String, value: T?) {
        this.code =code
        this.msg =msg
        this.value =value
        this.success =success
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}