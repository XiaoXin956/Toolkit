package com.xiaoxin.toolkit.repository

import com.google.gson.reflect.TypeToken
import com.xiaoxin.basic.utils.GsonUtils
import com.xiaoxin.toolkit.bean.Hitokoto

class HitokotoRepository : BaseRepository() {

    suspend fun getHitokoto(
        url: String,
        queryMap: Map<String, Any>
    ): Hitokoto? {
        var hitokoto: Hitokoto? = null
        retrofitManager
            .getMethod
            .setUrl(url)
            .setQueryMap(queryMap)
            .requestT(
                success = {
                    hitokoto =  GsonUtils.toObject(it.toString(), object : TypeToken<Hitokoto>() {}.type)
                },
                error = {
                    hitokoto = Hitokoto()
                    hitokoto?.run {
                        id=-1
                        this.hitokoto = "未知"
                    }
                }
            )
        return hitokoto
    }


}