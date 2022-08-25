package com.xiaoxin.basic.utils

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * @author: Admin
 * @date: 2021-12-02
 */
object GsonUtils {

    var gson = GsonBuilder().create()
    fun toJson(obj: Any?): String {
        return gson.toJson(obj)
    }

    fun <T> toObject(resJson: String?): T {
        return gson.fromJson(resJson.toString(), object : TypeToken<T>() {}.type)
    }

    fun <T> toObject(resJson: String?, type: Type): T {
        return gson.fromJson(resJson, type)
    }

}