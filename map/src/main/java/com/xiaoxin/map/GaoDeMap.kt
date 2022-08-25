package com.xiaoxin.map

/**
 * 高德
 */
class GaoDeMap :IMap {
    override suspend fun addressCode(key: String, maps: HashMap<String, Any>): MapHttpResponse<GoogleBean.GeoCodes> {
        TODO("Not yet implemented")
    }


}