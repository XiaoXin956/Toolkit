package com.xiaoxin.network.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xiaoxin.network.IAPIService
import com.xiaoxin.network.interceptors.CustomInterceptors
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: Admin
 * @date: 2022-03-25
 */
class RetrofitManagerDownLoad(private var downLoadListener: ((Long) -> Unit)? = null) {

    private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(CustomInterceptors.ProgressInterceptor(downLoadListener))
                .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://www.xiaoxin.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val gson: Gson by lazy {
        GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create()
    }

    fun apiService(): IAPIService {
        return retrofit.create(IAPIService::class.java)
    }

}