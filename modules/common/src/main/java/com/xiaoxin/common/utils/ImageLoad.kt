package com.xiaoxin.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

/**
 * @author: Admin
 * @date: 2021-08-24
 */
object ImageLoad {

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param imagePath 图片地址
     * @param imageView 显示的控件
     */
    fun glideLoad(context: Context?, imagePath: Any?, imageView: ImageView?, isCircle: Boolean) {
        if (isCircle) {
            Glide.with(context!!).load(imagePath).apply(RequestOptions.bitmapTransform(CircleCrop())).into(imageView!!)
        } else {
            Glide.with(context!!).load(imagePath).into(imageView!!)
        }
    }


    fun loadCirclePic(context: Context?, url: String?, imageView: ImageView?) {
        val requestOptions = RequestOptions.circleCropTransform()
        Glide.with(context!!)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView!!)
    }

    @BindingAdapter(value = ["imgUrl"], requireAll = false)
    @JvmStatic
    fun loadImage(imageView: ImageView, url: Any?) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

}