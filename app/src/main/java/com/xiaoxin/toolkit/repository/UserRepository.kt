package com.xiaoxin.toolkit.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiaoxin.common.file.saveFile
import com.xiaoxin.network.retrofit.RetrofitManagerDownLoad
import com.xiaoxin.network.retrofit.RetrofitManager
import com.xiaoxin.toolkit.HttpResponse
import com.xiaoxin.toolkit.http.BaseException
import java.lang.Exception
import java.util.*

/**
 * @author: Admin
 * @date: 2022-03-22
 */
class UserRepository {

    private val retrofitManagerLiveData by lazy {
        RetrofitManager()
            .getInstance()
    }

    suspend fun login(url: String, maps: HashMap<String, String>): HttpResponse<Any> {
        var httpResponse: HttpResponse<Any>? = null
        retrofitManagerLiveData.postMethod
            .setUrl(url)
            .setDataMap(maps)
            .requestT(
                success = {
                    httpResponse = Gson().fromJson<HttpResponse<Any>>(
                        it.toString(),
                        object : TypeToken<HttpResponse<Any>>() {}.type
                    )
                },
                error = {
                    httpResponse = BaseException.exception(it)
                }
            )
        return httpResponse!!
    }

    suspend fun upLoadFile(
        url: String,
        headerMap: HashMap<String, Any>,
        data: HashMap<String, Any>,
        uploadListener: ((Long) -> Unit)
    ): HttpResponse<List<Any>> {
        var httpResponse: HttpResponse<List<Any>>? = null
        retrofitManagerLiveData.postMethod
            .setUrl(url = url)
            .setHeaderMap(headerMap)
            .setDataMap(data)
            .setLoadMonitorListener(uploadListener)
            .requestT(
                success = {
                    val gson = Gson()
                    val toJson = gson.toJson(it)
                    httpResponse = Gson().fromJson<HttpResponse<List<Any>>>(
                        toJson,
                        object : TypeToken<HttpResponse<List<Any>>>() {}.type
                    )
                },
                error = {
                    httpResponse = BaseException.exception(it)
                }
            )
        return httpResponse!!
    }

    suspend fun downLoadFile(
        url: String,
        headers: HashMap<String, Any>,
        savePath: String,
        downLoadListener: ((Long) -> Unit)? = null
    ): HttpResponse<Any> {
        var httpResponse: HttpResponse<Any>? = try {
            val download = RetrofitManagerDownLoad(downLoadListener)
                .apiService()
                .download(url = url, headers = headers)
            saveFile(savePath, inputStream = download.byteStream())
            // 对文件进行处理
            HttpResponse(0, "", download)
        } catch (ex: Exception) {
            BaseException.exception(ex)
        }
        return httpResponse!!
    }

}