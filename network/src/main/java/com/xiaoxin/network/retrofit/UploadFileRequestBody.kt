package com.xiaoxin.network.retrofit

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.*
import java.io.File
import java.io.IOException

/**
 * 文件上传
 * @property requestBody RequestBody
 * @property upMethod IUpLoadMonitor
 * @property bufferedSink BufferedSink?
 * @property percent Long
 */
class UploadFileRequestBody : RequestBody {

    private var requestBody: RequestBody
    private var upMethod: ((Long) -> Unit)? = null
    private var bufferedSink: BufferedSink? = null
    private var percent: Long = 0

    constructor(file: File, upMethod: ((Long) -> Unit)) {
        requestBody = create(MediaType.parse("multipart/form-data"), file)
        this.upMethod = upMethod
    }

    constructor(requestBody: RequestBody, upMethod: ((Long) -> Unit)) {
        this.requestBody = requestBody
        this.upMethod = upMethod
    }

    constructor(maps: Map<String, Any?>, upMethod: ((Long) -> Unit)) {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (key in maps.keys) {
            // 判断是多文件还是单文件
            when (val value = maps[key]) {
                is File -> {
                    multipartBody.addFormDataPart(key, value.name, create(MediaType.parse(fileType(value)), value))
                }
                is List<*> -> {
                    for (file in value) {
                        if (file is File) {
                            multipartBody.addFormDataPart(key, file.name, create(MediaType.parse(fileType(file)), file))
                        }
                    }
                }
                else -> {
                    multipartBody.addFormDataPart(key, value.toString())
                }
            }
        }
        requestBody = multipartBody.build()
        this.upMethod = upMethod
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink))
        }
        requestBody.writeTo(bufferedSink!!)
        bufferedSink!!.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            var bytesWritten = 0L

            //总字节长度，避免多次调用contentLength()方法
            var contentLength = 0L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    contentLength = contentLength()
                }
                bytesWritten += byteCount
                percent = ((bytesWritten * 100 / contentLength).toInt()).toLong()
                if (percent > 100) {
                    percent = 100
                }
                if (percent < 0) {
                    percent = 0
                }
                upMethod?.invoke(percent)
            }
        }
    }


    private fun fileType(file: File): String {
        var type = "application/json"
        val extensionType = file.name.lastIndexOf(".").toString()
        if (extensionType == "jpg" || extensionType == "jpeg" || extensionType == "jpe") {
            type = "image/jpeg"
        } else if (extensionType == "png") {
            type = "image/png"
        } else if (extensionType == "svg") {
            type = "image/svg+xml"
        } else if (extensionType == "gif") {
            type = "image/gif"
        } else if (extensionType == "mp3") {
            type = "audio/mpeg"
        } else if (extensionType == "avi") {
            type = "video/x-msvideo"
        } else if (extensionType == "xml") {
            type = "application/xml"
        } else if (extensionType == "js") {
            type = "application/x-javascript"
        } else if (extensionType == "ppt") {
            type = "application/vnd.ms-powerpoint"
        } else if (extensionType == "txt") {
            type = "text/plain"
        } else if (extensionType == "zip") {
            type = "application/zip"
        }
        return type
    }

}