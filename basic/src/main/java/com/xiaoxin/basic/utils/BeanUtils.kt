package com.xiaoxin.basic.utils

fun <T, N> copyProperties(old: T, new: Class<N>): N {
    val oldJson = GsonUtils.toJson(old)
    return GsonUtils.gson.fromJson(oldJson, new)
}