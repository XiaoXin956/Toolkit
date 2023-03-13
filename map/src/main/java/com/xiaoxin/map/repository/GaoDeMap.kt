package com.xiaoxin.map.repository

import com.xiaoxin.map.IMap
import com.xiaoxin.map.base.Resource

/**
 * 高德
 */
class GaoDeMap : IMap {

    private val googleMapRepository by lazy { GoogleMapRepository() }

    override suspend fun addressCode(
        key: String,
        maps: HashMap<String, Any>
    ): Resource<Any> {
        return try {
            val googleCode = googleMapRepository.googleCode(key, maps = maps)
            if (googleCode.code == 0) {
                Resource.Success(data = googleCode.value)
            } else {
                Resource.Error(message = googleCode.msg, data = googleCode.value)
            }
        } catch (ex: Exception) {
            Resource.Error(message = "出现异常")
        }
    }


}