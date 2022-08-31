package com.xiaoxin.map.base

sealed class Resource<T>(
    var code: Int? = null,
    var data: T? = null,
    var message: String? = null
) {

    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(message = message, data = data)

}
