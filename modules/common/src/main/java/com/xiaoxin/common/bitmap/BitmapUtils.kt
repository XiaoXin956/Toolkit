package com.xiaoxin.common.bitmap

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import java.io.File
import java.io.FileOutputStream


/**
 * Bitmap
 * @author: Admin
 * @date: 2021-08-25
 */
class BitmapUtils {

    /**
     * 保存 Bitmap 为图片文件
     * @param bitmap Bitmap
     * @param newFilePath String
     * @return File
     */
    fun saveBitmap(bitmap: Bitmap, newFilePath: String): File {
        val bitmapOut = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        val canvas = Canvas(bitmapOut)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val file = File(newFilePath)
        if (!file.exists()) {
            file.parentFile!!.mkdirs()
            file.createNewFile()
        }
        val fileOutputStream = FileOutputStream(file)
        if (bitmapOut.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)){
            fileOutputStream.flush()
            fileOutputStream.close()
        }
        return file
    }

    /**
     * view 转为 Bitmap
     * @param activity Activity
     * @param v View
     * @param newImgPath String
     * @return File
     */
    fun loadViewBitmap(activity: Activity, v: View, newImgPath:String): File {
        val w = v.width;
        val h = v.height
        val bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        v.layout(0,0,w,h)
        v.draw(canvas)
        return saveBitmap(bitmap,newImgPath)
    }

    fun bitmap(activity: Activity):Bitmap{
        var view = activity.window.decorView;
        view.buildDrawingCache()
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect);
        val statusBarHeights = rect.top
        val display = activity.windowManager.defaultDisplay
        // 获取屏幕宽和高
        // 获取屏幕宽和高
        val widths = display.width
        val heights = display.height
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        var bmp = Bitmap.createBitmap(view.drawingCache,0,statusBarHeights,widths,statusBarHeights)
        view.destroyDrawingCache();
        return bmp;
    }

}