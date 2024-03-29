package com.xiaoxin.network.retrofit.method

import com.google.gson.Gson
import com.xiaoxin.network.retrofit.UploadFileRequestBody
import com.xiaoxin.network.retrofit.IAPIService
import okhttp3.*
import java.lang.Exception
import kotlin.collections.HashMap

/**
 * @author: Admin
 * @date: 2021-12-03
 */
class PostMethod() : BaseParam() {

    private var api: IAPIService? = null
    private var dataMap: Map<String, Any> = HashMap()
    private var loadMonitorListener: ((Long) -> Unit)? = null
    private var dataAny:Any = Any()

    constructor(api: IAPIService) : this() {
        this.api = api
    }

    fun setUrl(url: String): PostMethod {
        this.url = url
        return this
    }

    fun setHeaderMap(headers: HashMap<String, Any>): PostMethod {
        this.headers = headers
        return this
    }

    fun addHeader(header:String,value:Any):PostMethod{
        this.headers.let {
            it[header] = value
        }
        return this
    }

    fun setDataMap(dataMap: Map<String, Any>): PostMethod {
        this.dataMap = dataMap
        return this
    }

    fun  setDataAny(dataAny:Any):PostMethod{
        this.dataAny = dataAny
        return this
    }

    fun setRequestType(requestType: HttpParamWay): PostMethod {
        this.requestType = requestType
        return this
    }

    fun setLoadMonitorListener(loadMonitorListener: ((Long) -> Unit)): PostMethod {
        this.loadMonitorListener = loadMonitorListener
        return this
    }

    override suspend fun requestT(
        success: ((Any) -> Unit)?,
        error: ((Any) -> Unit)?
    ) {
        val requestBody: RequestBody = if (loadMonitorListener != null) {
            UploadFileRequestBody(dataMap, loadMonitorListener!!)
        } else {
            RequestBody.create(MediaType.parse("application/json"), Gson().toJson(dataMap))
        }
        try {
            val post = if (requestType == HttpParamWay.FORM) {
                api?.post(url = url, headers = headers, fieldMap = dataMap)
            } else {
                api?.post(url = url, headers = headers, requestBody = requestBody)
            }
            success?.invoke(gson.toJson(post))
        } catch (ex: Exception) {
            error?.invoke(ex)
        }
    }

    override suspend fun requestAny(success: ((Any) -> Unit)?, error: ((Any) -> Unit)?) {
        val requestBody: RequestBody = if (loadMonitorListener != null) {
            UploadFileRequestBody(dataMap, loadMonitorListener!!)
        } else {
            RequestBody.create(MediaType.parse("application/json"), Gson().toJson(dataAny))
        }
        try {
            val post = if (requestType == HttpParamWay.FORM) {
                api?.post(url = url, headers = headers, fieldMap = dataMap)
            } else {
                api?.post(url = url, headers = headers, requestBody = requestBody)
            }
            success?.invoke(gson.toJson(post))
        } catch (ex: Exception) {
            error?.invoke(ex)
        }
    }
}