package com.xiaoxin.network.retrofit.bean

import android.net.ParseException
import android.util.MalformedJsonException
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object BaseException {

    fun <T> exception(any: Any): BaseHttpResponse<T> {
        return when (any) {
            is HttpException -> {
                httpException(any)
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                BaseHttpResponse(HttpResult.ParseException.code, HttpResult.ParseException.msg)
            }
            is ConnectException -> { // 连接异常
                BaseHttpResponse(HttpResult.ConnectException.code, HttpResult.ConnectException.msg)
            }
            is UnknownHostException -> { // 主机异常
                BaseHttpResponse(
                    HttpResult.UnknownHostException.code,
                    HttpResult.UnknownHostException.msg
                )
            }
            is SocketTimeoutException -> { // 连接
                BaseHttpResponse(
                    HttpResult.SocketTimeoutException.code,
                    HttpResult.SocketTimeoutException.msg
                )
            }
            is FileNotFoundException -> { // 连接
                BaseHttpResponse(
                    HttpResult.FileNotFoundException.code,
                    HttpResult.FileNotFoundException.msg
                )
            }
            else -> { // 未捕捉异常，统一处理
                BaseHttpResponse(HttpResult.ERROR.code, HttpResult.ERROR.msg)
            }
        }
    }


    private fun <T> httpException(e: HttpException): BaseHttpResponse<T> {
        val httpError: BaseHttpResponse<T>?
        when (e.code()) {
            200 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP200.code, msg = HttpResult.HTTP200.msg)
            }
            201 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP201.code, msg = HttpResult.HTTP201.msg)
            }
            202 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP202.code, msg = HttpResult.HTTP202.msg)
            }
            300 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP300.code, msg = HttpResult.HTTP300.msg)
            }
            301 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP301.code, msg = HttpResult.HTTP301.msg)
            }
            302 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP302.code, msg = HttpResult.HTTP302.msg)
            }
            303 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP303.code, msg = HttpResult.HTTP303.msg)
            }
            304 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP304.code, msg = HttpResult.HTTP304.msg)
            }
            305 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP305.code, msg = HttpResult.HTTP305.msg)
            }
            306 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP306.code, msg = HttpResult.HTTP306.msg)
            }
            400 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP400.code, msg = HttpResult.HTTP400.msg)
            }
            401 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP401.code, msg = HttpResult.HTTP401.msg)
            }
            402 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP402.code, msg = HttpResult.HTTP402.msg)
            }
            404 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP404.code, msg = HttpResult.HTTP404.msg)
            }
            405 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP405.code, msg = HttpResult.HTTP405.msg)
            }
            406 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP406.code, msg = HttpResult.HTTP406.msg)
            }
            407 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP407.code, msg = HttpResult.HTTP407.msg)
            }
            408 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP408.code, msg = HttpResult.HTTP408.msg)
            }
            409 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP409.code, msg = HttpResult.HTTP409.msg)
            }
            410 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP410.code, msg = HttpResult.HTTP410.msg)
            }
            411 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP411.code, msg = HttpResult.HTTP411.msg)
            }
            412 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP412.code, msg = HttpResult.HTTP412.msg)
            }
            413 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP413.code, msg = HttpResult.HTTP413.msg)
            }
            414 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP414.code, msg = HttpResult.HTTP414.msg)
            }
            415 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP415.code, msg = HttpResult.HTTP415.msg)
            }
            416 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP416.code, msg = HttpResult.HTTP416.msg)
            }
            417 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP417.code, msg = HttpResult.HTTP417.msg)
            }
            500 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP500.code, msg = HttpResult.HTTP500.msg)
            }
            501 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP501.code, msg = HttpResult.HTTP501.msg)
            }
            502 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP502.code, msg = HttpResult.HTTP502.msg)
            }
            503 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP503.code, msg = HttpResult.HTTP503.msg)
            }
            504 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP504.code, msg = HttpResult.HTTP504.msg)
            }
            505 -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.HTTP505.code, msg = HttpResult.HTTP505.msg)
            }
            else -> {
                httpError =
                    BaseHttpResponse(code = HttpResult.ERROR.code, msg = HttpResult.ERROR.msg)
            }
        }
        return httpError
    }


}