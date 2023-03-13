package com.xiaoxin.network.retrofit.method

import com.xiaoxin.network.retrofit.IAPIService
import java.util.*

/**
 * @author: Admin
 * @date: 2021-12-03
 */
class GetMethod() : BaseParam() {

    private var api: IAPIService? = null
    private var queryMap: Map<String, Any> = HashMap()
    private var loadMonitorListener: ((Long) -> Unit)? = null

    constructor(api: IAPIService) : this() {
        this.api = api
    }

    fun setUrl(url: String): GetMethod {
        this.url = url;
        return this
    }

    fun setHeaderMap(headers: HashMap<String, Any>): GetMethod {
        this.headers = headers
        return this
    }

    fun addHeader(header: String, value: Any): GetMethod {
        this.headers.let {
            it[header] = value
        }
        return this
    }

    fun setQueryMap(queryMap: Map<String, Any>): GetMethod {
        this.queryMap = queryMap
        return this
    }

    fun setLoadMonitorListener(loadMonitorListener: ((Long) -> Unit)): GetMethod {
        this.loadMonitorListener = loadMonitorListener
        return this
    }

    override suspend fun requestT(
        success: ((Any) -> Unit)?,
        error: ((Any) -> Unit)?
    ) {
        try {
            val get = if (queryMap.isEmpty()) {
                api?.get(url = url, headers = headers)
            } else {
                api?.get(url = url, headers = headers, map = queryMap)
            }
            success?.invoke(gson.toJson(get))
        } catch (ex: Exception) {
            error?.invoke(ex)
        }
    }

    override suspend fun request(): Any? {
        return super.request()
    }
}