package com.xiaoxin.toolkit.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiaoxin.basic.file.saveFile
import com.xiaoxin.network.retrofit.RetrofitManagerDownLoad
import com.xiaoxin.network.retrofit.bean.BaseException
import com.xiaoxin.toolkit.HttpResponse
import java.lang.Exception
import java.util.*

/**
 * @author: Admin
 * @date: 2022-03-22
 */
class UserRepository : BaseRepository() {

    suspend fun login(url: String, maps: HashMap<String, String>): HttpResponse<Any> {

        var httpResponse: HttpResponse<Any>? = null
        retrofitManager
            .postMethod
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
                    httpResponse = BaseException.exception<Any>(it) as HttpResponse<Any>
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
        retrofitManager.postMethod
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
                    httpResponse = BaseException.exception<List<Any>>(it) as HttpResponse<List<Any>>
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
            BaseException.exception<Any>(ex) as HttpResponse<Any>
        }
        return httpResponse!!
    }

    suspend fun msg(url: String, maps: HashMap<String, String>): HttpResponse<Any> {

        var httpResponse: HttpResponse<Any>? = null
        customizeRetrofitManager
            .getInstance()
            .postMethod
            .setUrl(url = url)
            .setDataMap(dataMap = maps)
            .requestT(
                success = {
                    httpResponse = Gson().fromJson<HttpResponse<Any>>(
                        it.toString(),
                        object : TypeToken<HttpResponse<Any>>() {}.type
                    )
                },
                error = {
                    httpResponse = BaseException.exception<Any>(it) as HttpResponse<Any>
                }
            )
        return httpResponse!!
    }

}