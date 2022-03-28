package com.xiaoxin.network

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.*

/**
 * 下载
 * @property responseBody ResponseBody?
 * @property downLoadListener Function1<Long, Unit>?
 * @property bufferedSource BufferedSource?
 */
class DownLoadResponseBody(responseBody: ResponseBody, upMethod: ((Long) -> Unit)) :
    ResponseBody() {

    var responseBody: ResponseBody? = responseBody
    var downLoadListener: ((Long) -> Unit)? = upMethod
    var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength()!!
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody!!.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            var contentLength = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (contentLength==0L) {
                    contentLength = contentLength()
                }
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                val progress=  (100 * totalBytesRead) / contentLength
                downLoadListener?.invoke(progress)
                return bytesRead
            }
        }
    }


}