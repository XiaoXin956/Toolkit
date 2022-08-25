package com.xiaoxin.map.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiaoxin.basic.utils.copyProperties
import com.xiaoxin.map.GoogleBean
import com.xiaoxin.map.MapHttpResponse
import com.xiaoxin.map.constant.ConstantMap
import com.xiaoxin.network.retrofit.bean.BaseException

class GoogleMapRepository : BaseMapRepository() {

    /**
     * 地理编码
     * 仅提供一个  address   location    placeId
     * 可选：bounds    componentRestrictions   region
     */
    suspend fun googleCode(
        key: String,
        maps: HashMap<String, Any>
    ): MapHttpResponse<GoogleBean.GeoCodes> {
        var result = MapHttpResponse<GoogleBean.GeoCodes>()
        var geoCodes: GoogleBean.GeoCodes?
        if (maps["address"] != null) {
            maps["address"]
            maps.remove("location")
            maps.remove("placeId")
        } else if (maps["location"] != null) {
            maps["location"]
            maps.remove("address")
            maps.remove("placeId")
        } else {
            maps["placeId"]
            maps.remove("address")
            maps.remove("location")
        }
        maps["key"] = key

        retrofitManager
            .getMethod
            .setUrl(ConstantMap.GoogleMap.geocoding)
            .setQueryMap(maps)
            .requestT(
                success = {
                    geoCodes = Gson().fromJson(
                        it.toString(),
                        object : TypeToken<GoogleBean.GeoCodes>() {}.type
                    )
                    result.code = 0
                    result.value = geoCodes
                },
                error = {

                    val baseHttpResponse = BaseException.exception<GoogleBean.GeoCodes>(it)
                    result =
                        copyProperties(
                            baseHttpResponse,
                            MapHttpResponse::class.java
                        ) as MapHttpResponse<GoogleBean.GeoCodes>
                    result.code = baseHttpResponse.code
                    result.msg = baseHttpResponse.msg
                    result.value = baseHttpResponse.value
                }
            )
        return result
    }


}