package com.xiaoxin.toolkit.http

import android.net.ParseException
import android.util.Log
import android.util.MalformedJsonException
import com.google.gson.JsonParseException
import com.xiaoxin.toolkit.HttpResponse
import org.json.JSONException
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author: Admin
 * @date: 2022-03-24
 */
class BaseException {

    companion object {
        fun <T> exception(any: Any): HttpResponse<T> {
            return if (any is HttpException) {
                httpException(any)
            } else if (any is JsonParseException || any is JSONException || any is ParseException || any is MalformedJsonException) {
                HttpResponse(HttpResult.ParseException.code, HttpResult.ParseException.msg)
            } else if (any is ConnectException) { // 连接异常
                HttpResponse(HttpResult.ConnectException.code, HttpResult.ConnectException.msg)
            } else if (any is UnknownHostException) { // 主机异常
                HttpResponse(HttpResult.UnknownHostException.code, HttpResult.UnknownHostException.msg)
            } else if (any is SocketTimeoutException) { // 连接
                HttpResponse(HttpResult.SocketTimeoutException.code, HttpResult.SocketTimeoutException.msg)
            } else if (any is FileNotFoundException) { // 连接
                HttpResponse(HttpResult.FileNotFoundException.code, HttpResult.FileNotFoundException.msg)
            } else { // 未捕捉异常，统一处理
                HttpResponse(HttpResult.ERROR.code, HttpResult.ERROR.msg)
            }
        }

        private fun <T> httpException(e: HttpException): HttpResponse<T> {
            val httpError: HttpResponse<T>?
             when (e.code()) {
                200 -> {
                    httpError = HttpResponse(code = HttpResult.HTTP200.code, msg = HttpResult.HTTP200.msg)
                }
                201 -> {
                    httpError  = HttpResponse(code = HttpResult.HTTP201.code, msg = HttpResult.HTTP201.msg)
                }
                202 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP202.code, msg = HttpResult.HTTP202.msg)
                }
                300 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP300.code, msg = HttpResult.HTTP300.msg)
                }
                301 -> {
                    httpError = HttpResponse(code = HttpResult.HTTP301.code, msg = HttpResult.HTTP301.msg)
                }
                302 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP302.code, msg = HttpResult.HTTP302.msg)
                }
                303 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP303.code, msg = HttpResult.HTTP303.msg)
                }
                304 -> {
                    httpError = HttpResponse(code = HttpResult.HTTP304.code, msg = HttpResult.HTTP304.msg)
                }
                305 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP305.code, msg = HttpResult.HTTP305.msg)
                }
                306 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP306.code, msg = HttpResult.HTTP306.msg)
                }
                400 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP400.code, msg = HttpResult.HTTP400.msg)
                }
                401 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP401.code, msg = HttpResult.HTTP401.msg)
                }
                402 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP402.code, msg = HttpResult.HTTP402.msg)
                }
                404 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP404.code, msg = HttpResult.HTTP404.msg)
                }
                405 -> {
                    httpError =    HttpResponse(code = HttpResult.HTTP405.code, msg = HttpResult.HTTP405.msg)
                }
                406 -> {
                    httpError =  HttpResponse(code = HttpResult.HTTP406.code, msg = HttpResult.HTTP406.msg)
                }
                407 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP407.code, msg = HttpResult.HTTP407.msg)
                }
                408 -> {
                    httpError =    HttpResponse(code = HttpResult.HTTP408.code, msg = HttpResult.HTTP408.msg)
                }
                409 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP409.code, msg = HttpResult.HTTP409.msg)
                }
                410 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP410.code, msg = HttpResult.HTTP410.msg)
                }
                411 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP411.code, msg = HttpResult.HTTP411.msg)
                }
                412 -> {
                    httpError =    HttpResponse(code = HttpResult.HTTP412.code, msg = HttpResult.HTTP412.msg)
                }
                413 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP413.code, msg = HttpResult.HTTP413.msg)
                }
                414 -> {
                    httpError =HttpResponse(code = HttpResult.HTTP414.code, msg = HttpResult.HTTP414.msg)
                }
                415 -> {
                    httpError =    HttpResponse(code = HttpResult.HTTP415.code, msg = HttpResult.HTTP415.msg)
                }
                416 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP416.code, msg = HttpResult.HTTP416.msg)
                }
                417 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP417.code, msg = HttpResult.HTTP417.msg)
                }
                500 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP500.code, msg = HttpResult.HTTP500.msg)
                }
                501 -> {
                    httpError =    HttpResponse(code = HttpResult.HTTP501.code, msg = HttpResult.HTTP501.msg)
                }
                502 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP502.code, msg = HttpResult.HTTP502.msg)
                }
                503 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP503.code, msg = HttpResult.HTTP503.msg)
                }
                504 -> {
                    httpError =   HttpResponse(code = HttpResult.HTTP504.code, msg = HttpResult.HTTP504.msg)
                }
                505 -> {
                    httpError =    HttpResponse(code = HttpResult.HTTP505.code, msg = HttpResult.HTTP505.msg)
                }
                else -> {
                    httpError =   HttpResponse(code = HttpResult.ERROR.code, msg = HttpResult.ERROR.msg)
                }
            }
            return httpError
        }
    }


}