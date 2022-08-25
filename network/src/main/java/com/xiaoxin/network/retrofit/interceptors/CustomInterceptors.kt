package com.xiaoxin.network.retrofit.interceptors

import android.util.Log
import com.xiaoxin.network.retrofit.DownLoadResponseBody
import com.xiaoxin.network.retrofit.LogConfig
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.lang.invoke.MethodType
import java.lang.reflect.Method
import java.nio.charset.Charset

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
            if (LogConfig.printLog) {
                val request = chain.request()
                if (request.method() == "GET") {
                    return chain.proceed(chain.request())
                }
                val requestBody = request.body()
                val charSet: Charset = Charset.forName("UTF-8")
                requestBody?.contentType()!!.charset(charSet)
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                Log.d(
                    "请求",
                    "方法：${request.method()}地址：${
                        request.url().url()
                    } 数据：${buffer.readString(charSet)}"
                )

                val response = chain.proceed(request)
                val responseBody = response.body()!!
                val res = String(responseBody.bytes())
                Log.d("获取", "地址：${request.url().url()} 数据：${res}")
                return response.newBuilder()
                    .body(ResponseBody.create(responseBody.contentType(), res))
                    .build()
            }
            return chain.proceed(chain.request())
        }
    }

    /**
     * 下载拦截器
     * @property upMethod Function1<Long, Unit>?
     * @constructor
     */
    class ProgressInterceptor(var upMethod: ((Long) -> Unit)? = null) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if (LogConfig.printLog) {
                val request = chain.request()
                val requestBody = request.body()
                val charSet: Charset = Charset.forName("UTF-8")
                requestBody?.contentType()?.charset(charSet)
                val buffer = Buffer()
                requestBody?.writeTo(buffer)
                Log.d(
                    "请求",
                    "方法：${request.method()}地址：${
                        request.url().url()
                    } 数据：${buffer.readString(charSet)}"
                )
                val response = chain.proceed(request)
                val responseBody = response.body()!!
                return response.newBuilder()
                    .body(DownLoadResponseBody(responseBody, upMethod!!))
                    .build()
            } else {
                val response = chain.proceed(chain.request())
                val responseBody = response.body()
                return response
                    .newBuilder()
                    .body(DownLoadResponseBody(responseBody!!, upMethod!!))
                    .build()
            }

        }
    }

}