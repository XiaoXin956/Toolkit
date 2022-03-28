package com.xiaoxin.network

/**
 * @author: Admin
 * @date: 2021-12-02
 */
open class BaseResponse<T> {

    var code = 0
    var msg: String? = null
    var value: T? = null

    constructor() {}

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, msg: String?) {
        this.code = code
        this.msg = msg
    }

    constructor(code: Int, msg: String?, value: T) {
        this.code = code
        this.msg = msg
        this.value = value
    }

}