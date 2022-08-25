package com.xiaoxin.basic.mvvm

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageLoad {

    @BindingAdapter(value = ["imgUrl"], requireAll = false)
    @JvmStatic
    fun loadImage(imageView: ImageView, url: Any?) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }


}