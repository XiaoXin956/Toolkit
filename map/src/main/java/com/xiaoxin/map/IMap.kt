package com.xiaoxin.map

import androidx.annotation.Nullable
import com.xiaoxin.map.base.Resource

/**
 * @author: Admin
 * @date: 2022-05-07
 */
interface IMap {

    // 当前位置


    // 路线数据

    // 地理/逆地理编码
    suspend fun addressCode(@Nullable key: String, maps: HashMap<String, Any>):Resource<Any>
            //MapHttpResponse<GoogleBean.GeoCodes>

    //距离
//    fun distance(leftTopLatlng: String, rightBottomLatlng: String)

    // 面积
//    fun acreage(latLngStart: String, latLngEnd: String)

}