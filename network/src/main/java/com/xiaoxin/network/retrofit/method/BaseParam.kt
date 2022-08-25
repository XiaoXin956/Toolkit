package com.xiaoxin.network.retrofit.method

import kotlin.collections.HashMap

/**
 * @author: Admin
 * @date: 2021-12-02
 */
abstract class BaseParam {

    protected lateinit var url: String
    protected var headers: HashMap<String, Any> = HashMap()
    protected var requestType: HttpParamWay = HttpParamWay.NONE

    open suspend fun requestT(
        success: ((Any) -> Unit)? = null,
        error: ((Any) -> Unit)? = null
    ) {

    }

}