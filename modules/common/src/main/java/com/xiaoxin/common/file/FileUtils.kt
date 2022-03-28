package com.xiaoxin.common.file

import android.app.Application
import android.content.Context
import com.xiaoxin.common.utils.GsonUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 保存文件
 * @param filePath String
 * @param fileName String
 * @param inputStream InputStream
 */
fun saveFile(filePath: String, inputStream: InputStream) {

    val file = File(filePath)
    if (file.parentFile!!.exists()) {
        // 创建文件夹
        file.mkdirs()
        file.createNewFile()
    }
    val buf = ByteArray(2048)
    val fos = FileOutputStream(file)
    var len: Int
    while ((inputStream.read(buf).also { len = it }) != -1) {
        fos.write(buf, 0, len)
        fos.flush()
    }
    inputStream.close()
    fos.flush()
    fos.close()

}

/**
 * 解析Assets 文件
 * @param application Application
 * @param filePath String?
 * @return T?
 */
fun <T> getAssets(application: Application, filePath: String?): List<T>? {
    val result: String
    try {
        val inputStreamReader = application.resources.assets.open(filePath!!)
        val bytes = ByteArray(inputStreamReader.available())
        inputStreamReader.read(bytes)
        result = String(bytes)
        inputStreamReader.close()
        // 转换为对象
        return GsonUtils.toListObject(result)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}