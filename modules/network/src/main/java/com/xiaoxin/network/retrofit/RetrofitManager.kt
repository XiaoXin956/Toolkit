package com.xiaoxin.network.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xiaoxin.network.IAPIService
import com.xiaoxin.network.interceptors.CustomInterceptors
import com.xiaoxin.network.method.GetMethod
import com.xiaoxin.network.method.PostMethod
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: Admin
 * @date: 2021-12-02
 */
open class RetrofitManager {

    private var apiService: IAPIService? = null
    private var retrofit: Retrofit? = null
    private var baseRetrofitManager: RetrofitManager? = null

    private val gson: Gson by lazy {
        GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(70, TimeUnit.SECONDS) //连接服务器超时
            .readTimeout(70, TimeUnit.SECONDS) //读取服务器的数据超时
            .writeTimeout(70, TimeUnit.SECONDS) //写入本地的数据超时
            .addInterceptor(CustomInterceptors.HeaderInterceptors())
            .addInterceptor(CustomInterceptors.LoggingInterceptors())
            .build()
    }

    open fun getInstance(): RetrofitManager {

        if (baseRetrofitManager != null) {
            return this
        }
        baseRetrofitManager = RetrofitManager()
        apiService = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://www.xiaoxin.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IAPIService::class.java)
        return this
    }

    val postMethod: PostMethod
        get() = PostMethod(apiService!!)

    val getMethod: GetMethod
        get() = GetMethod(apiService!!)

}