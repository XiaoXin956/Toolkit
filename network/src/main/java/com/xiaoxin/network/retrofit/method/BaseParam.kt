package com.xiaoxin.network.retrofit.method

import com.google.gson.Gson
import kotlin.collections.HashMap

/**
 * @author: Admin
 * @date: 2021-12-02
 */
abstract class BaseParam {

    protected lateinit var url: String
    protected var headers: HashMap<String, Any> = HashMap()
    protected var requestType: HttpParamWay = HttpParamWay.NONE
    protected val gson:Gson = Gson()

    open suspend fun requestT(
        success: ((Any) -> Unit)? = null,
        error: ((Any) -> Unit)? = null
    ) { }

    open suspend fun requestAny(
        success: ((Any) -> Unit)? = null,
        error: ((Any) -> Unit)? = null
    ) { }

    open suspend fun request() :Any? {
        return null
    }

}