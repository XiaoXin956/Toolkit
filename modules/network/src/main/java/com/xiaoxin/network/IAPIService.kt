package com.xiaoxin.network

import androidx.lifecycle.LiveData
import com.xiaoxin.network.BaseResponse
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * @author: Admin
 * @date: 2021-12-02
 */
@JvmSuppressWildcards
interface IAPIService {

    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, Any>
    ): Any

    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, Any>,
        @QueryMap map: Map<String, Any>
    ): Any

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: Map<String, Any>,
        @FieldMap fieldMap: Map<String, Any>
    ): Any

    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: Map<String, Any>,
        @Body requestBody: RequestBody
    ): Any

    @Streaming
    @GET
    suspend fun download(
        @Url url: String,
        @HeaderMap headers: Map<String, Any>
    ): ResponseBody

}