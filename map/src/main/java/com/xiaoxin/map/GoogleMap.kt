package com.xiaoxin.map

import com.xiaoxin.map.repository.GoogleMapRepository

/**
 * 谷歌
 */
class GoogleMap : IMap {

    private val googleMapRepository by lazy { GoogleMapRepository() }

    override suspend fun addressCode(
        key: String,
        maps: HashMap<String, Any>
    ): MapHttpResponse<GoogleBean.GeoCodes> {
        return googleMapRepository.googleCode(key, maps = maps)
    }

}