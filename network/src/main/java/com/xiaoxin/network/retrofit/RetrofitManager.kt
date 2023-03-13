package com.xiaoxin.network.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xiaoxin.network.retrofit.interceptors.CustomInterceptors
import com.xiaoxin.network.retrofit.method.GetMethod
import com.xiaoxin.network.retrofit.method.PostMethod
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: Admin
 * @date: 2021-12-02
 */
open class RetrofitManager {

    constructor()
    constructor(builder: Builder) {
        this.baseUrl = builder.baseUrl
        this.customizeOkhttpClient = builder.okhttpClient
        this.customizeGson = builder.gson
    }
    private var baseUrl: String = ""
    private var customizeOkhttpClient: OkHttpClient? = null
    private var customizeGson: Gson? = null
    private var apiService: IAPIService? = null
    private var baseRetrofitManager: RetrofitManager? = null

    private var timeout:Long = 70

    private val gson: Gson by lazy {
        GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create()
    }
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS) //连接服务器超时
            .readTimeout(timeout, TimeUnit.SECONDS) //读取服务器的数据超时
            .writeTimeout(timeout, TimeUnit.SECONDS) //写入本地的数据超时
            .addInterceptor(CustomInterceptors.HeaderInterceptors())
            .addInterceptor(CustomInterceptors.LoggingInterceptors())
            .build()
    }

    fun getInstance(): RetrofitManager {
        if (baseRetrofitManager != null) {
            return this
        }
        baseRetrofitManager = RetrofitManager()
        val resOkHttpClient = if (customizeOkhttpClient == null) {
            okHttpClient
        } else {
            customizeOkhttpClient!!
        }
        val resGson = if (customizeGson == null) {
            gson
        } else {
            customizeGson!!
        }
        apiService = Retrofit.Builder()
            .client(resOkHttpClient)
            .baseUrl("http://www.xiaoxin.com")
            .addConverterFactory(GsonConverterFactory.create(resGson))
            .build()
            .create(IAPIService::class.java)
        return this
    }

    val postMethod: PostMethod
        get() {
            if (apiService == null) {
                throw NullPointerException("请先调用 getInstance() 初始化 ApiService ")
            }
            return PostMethod(apiService!!)
        }

    val getMethod: GetMethod
        get() {
            if (apiService == null) {
                throw NullPointerException("请先调用 getInstance() 初始化 ApiService ")
            }
            return GetMethod(apiService!!)
        }

    class Builder {
        // 地址
        var baseUrl: String = "http://www.xxx.com"
            private set
        // okhttp 配置
        var okhttpClient: OkHttpClient? = null
            private set
        // gson 配置
        var gson: Gson? = null
            private set

        fun setBaseUrl(baseUrl: String) = apply {
            this.baseUrl = baseUrl
        }

        fun setOkHttpClient(okhttpClient: OkHttpClient) = apply {
            this.okhttpClient = okhttpClient
        }

        fun setGson(gson: Gson) = apply {
            this.gson = gson
        }

        fun build() = RetrofitManager(this)

    }

}