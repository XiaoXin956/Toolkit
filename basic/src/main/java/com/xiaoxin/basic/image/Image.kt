package com.xiaoxin.basic.image

import android.graphics.drawable.Drawable
import android.widget.ImageView

interface Image {

    fun load(imageView: ImageView, url: Any)

    fun load(imageView: ImageView, url: Any, isCircle: Boolean)

    fun load(imageView: ImageView, url: Any, loading: Drawable?, error: Drawable?)




}