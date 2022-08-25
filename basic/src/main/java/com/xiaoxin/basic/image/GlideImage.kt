package com.xiaoxin.basic.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

class GlideImage:Image {

    override fun load(imageView: ImageView, url: Any) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

    override fun load(imageView: ImageView, url: Any, isCircle: Boolean) {
        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    override fun load(imageView: ImageView, url: Any, loading: Drawable?, error: Drawable?) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(loading)
            .error(error)
            .into(imageView)
    }
}