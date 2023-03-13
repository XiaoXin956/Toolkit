package com.xiaoxin.network.retrofit.interceptors

import android.util.Log
import com.xiaoxin.network.retrofit.DownLoadResponseBody
import com.xiaoxin.network.retrofit.LogConfig
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.nio.charset.Charset

class CustomInterceptors {

    /**
     * header 拦截器
     */
    open class HeaderInterceptors : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().addHeader("Content-Type", "application/json").addHeader("charset", "UTF-8").build()
            return chain.proceed(request)
        }
    }

    /**
     * 日志拦截器
     */
    open class LoggingInterceptors : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if (LogConfig.printLog) {
                var request = chain.request()

                if (request.method() == "GET") {
                    return chain.proceed(request)
                }
                val requestBody = request.body()
                val charset: Charset = Charset.forName("UTF-8")
                requestBody?.contentType()!!.charset(charset)
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                Log.d("network", "方法:${request.method()} 地址：${request.url().url()}  数据：${buffer.readString(charset)}")

                val response = chain.proceed(request)
                val responseBody = response.body()!!
                val res = String(responseBody.bytes())
                Log.d("network", "地址：${request.url().url()} 数据：${res}")
                return response.newBuilder()
                    .body(ResponseBody.create(responseBody.contentType(), res))
                    .build()
            }
            return chain.proceed(chain.request())
        }

    }

    /**
     * 下载拦截器
     */
    class ProgressInterceptor(var upMethod: ((Long) -> Unit)? = null) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            if (LogConfig.printLog) {
                val request = chain.request()
                val requestBody = request.body()
                val charset = Charset.forName("UTF-8")
                requestBody?.contentType()?.charset(charset)
                var buffer = Buffer()
                requestBody?.writeTo(buffer)
                Log.d("network", "方法：${request.method()}地址：${request.url().url()} 数据：${buffer.readString(charset)}")
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