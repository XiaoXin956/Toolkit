package com.xiaoxin.network.interceptors

import android.util.Log
import com.xiaoxin.network.DownLoadResponseBody
import com.xiaoxin.network.NetConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author: Admin
 * @date: 2021-12-02
 */
class CustomInterceptors {

  /**
   * header 拦截器
   */
  open class HeaderInterceptors : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
      val request = chain.request()
        .newBuilder()
        .addHeader("Content-Type", "application/json")
        .addHeader("charset", "UTF-8")
        .build()
      return chain.proceed(request)
    }
  }

  /**
   * 日志拦截器
   */
  class LoggingInterceptors : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
      val request = chain.request()
      val response = chain.proceed(request)
      if (NetConfig.printLog) {
        val body = response.body()
        val res = String(body!!.bytes())
        Log.d("拦截获取的数据", "地址：${request.url().url()}--数据：${res}")
      }
      return chain.proceed(request)
    }
  }

  /**
   * 下载拦截器
   * @property upMethod Function1<Long, Unit>?
   * @constructor
   */
  class ProgressInterceptor(var upMethod: ((Long) -> Unit)? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
      val response = chain.proceed(chain.request())
      val responseBody = response.body()
      return response
        .newBuilder()
        .body(DownLoadResponseBody(responseBody!!,upMethod!!))
        .build()
    }
  }

}