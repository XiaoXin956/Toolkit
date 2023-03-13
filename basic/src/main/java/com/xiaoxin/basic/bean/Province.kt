package com.xiaoxin.basic.bean

import com.google.gson.reflect.TypeToken
import com.xiaoxin.basic.utils.GsonUtils
import java.io.InputStream
import java.lang.Exception

class Province(val name: String, val city: ArrayList<City>) {
    override fun toString(): String {
        return "Province(${GsonUtils.toJson(this)})"
    }
}

class City(val name: String, val area: Array<String>) {
    override fun toString(): String {
        return "City(${GsonUtils.toJson(this)})"
    }
}

class Street(var name: String, val street: Array<String>)

/**
 * 省份数据
 * @return List<Province>
 */
fun getProvinceData(inputStream: InputStream): List<Province>? {
    val result: String
    try {
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        result = String(bytes)
        val type = object : TypeToken<List<Province>>() {}.type
        return GsonUtils.toObject<List<Province>>(result, type)
    } catch (ex: Exception) {
        inputStream.close()
    } finally {
        inputStream.close()
    }
    return null
}