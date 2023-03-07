package com.xiaoxin.toolkit.repository

import com.google.gson.GsonBuilder
import com.xiaoxin.network.retrofit.RetrofitManager
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @author: Admin
 * @date: 2022-03-29
 */
open class BaseRepository {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(11, TimeUnit.SECONDS) //连接服务器超时
            .readTimeout(11, TimeUnit.SECONDS) //读取服务器的数据超时
            .writeTimeout(11, TimeUnit.SECONDS) //写入本地的数据超时
            .build()
    }

    val customizeRetrofitManager by lazy {
        RetrofitManager.Builder()
            .setBaseUrl("")
            .setGson(GsonBuilder().create())
            .setOkHttpClient(okHttpClient)
            .build()
            .getInstance()
    }

    val retrofitManager by lazy {
        RetrofitManager()
            .getInstance()
    }

}