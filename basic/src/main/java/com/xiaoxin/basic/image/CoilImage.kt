package com.xiaoxin.basic.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import coil.load

class CoilImage :Image{
    override fun load(imageView: ImageView, url: Any) {
        imageView.load(url)
    }

    override fun load(imageView: ImageView, url: Any, isCircle: Boolean) {
        imageView.load(url){
        }
    }

    override fun load(imageView: ImageView, url: Any, loading: Drawable?, error: Drawable?) {
        imageView.load(url){
            placeholder(loading)
            error(error)

        }
    }
}