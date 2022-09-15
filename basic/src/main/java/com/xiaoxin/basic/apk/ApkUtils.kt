package com.xiaoxin.basic.apk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object ApkUtils {

    private fun installApk(context: Context, localFilePath: String, fileProvider: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val file = File(localFilePath)
            val intentQ = Intent(Intent.ACTION_VIEW)
            intentQ.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intentQ.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri = if (fileProvider != null) {
                FileProvider.getUriForFile(
                    context,
                    fileProvider,
                    file
                )
            } else {
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileProvider",
                    file
                )
            }
            intentQ.setDataAndType(uri, "application/vnd.android.package-archive")
            context.startActivity(intentQ)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkFile = File(localFilePath)
            val intent = Intent(Intent.ACTION_VIEW)
            val contentUri = if (fileProvider != null) {
                FileProvider.getUriForFile(
                    context,
                    fileProvider,
                    apkFile
                )
            } else {
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileProvider",
                    apkFile
                )
            }
            context.grantUriPermission(
                context.packageName,
                contentUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        } else {
            val apkFile = File(localFilePath)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }


}