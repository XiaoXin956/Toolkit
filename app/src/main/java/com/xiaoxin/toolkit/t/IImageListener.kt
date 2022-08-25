package com.xiaoxin.toolkit.t

import android.content.Context
import android.net.Uri
import android.widget.ImageView

/**
 * @author: Admin
 * @date: 2022-04-12
 */
interface IImageListener {
    fun display(context: Context, imageView: ImageView, url: String, progressId: Int, errorId: Int, tag: Any)
    fun display(context: Context, imageView: ImageView, url: String, progressId: Int, errorId: Int)
    fun display(context: Context, imageView: ImageView, url: String, progressId: Int)
    fun display(context: Context, imageView: ImageView, url: String)
    fun display(context: Context, imageView: ImageView, uri: Uri)
}

class GlideRequest : IImageListener {
    override fun display(context: Context, imageView: ImageView, url: String, progressId: Int, errorId: Int, tag: Any) {
    }

    override fun display(context: Context, imageView: ImageView, url: String, progressId: Int, errorId: Int) {
    }

    override fun display(context: Context, imageView: ImageView, url: String, progressId: Int) {
    }

    override fun display(context: Context, imageView: ImageView, url: String) {
    }

    override fun display(context: Context, imageView: ImageView, uri: Uri) {
    }

}

class PicassoRequest : IImageListener {
    override fun display(context: Context, imageView: ImageView, url: String, progressId: Int, errorId: Int, tag: Any) {
    }

    override fun display(context: Context, imageView: ImageView, url: String, progressId: Int, errorId: Int) {
    }

    override fun display(context: Context, imageView: ImageView, url: String, progressId: Int) {
    }

    override fun display(context: Context, imageView: ImageView, url: String) {
    }

    override fun display(context: Context, imageView: ImageView, uri: Uri) {
    }

}

object ImageRequestManager {

    const val typeGlide = "Glide"
    const val typePicasso = "Picasso"
    fun getRequest(type: String): IImageListener {
        return get(type)
    }

    private fun get(type: String): IImageListener {
        return when (type) {
            typeGlide -> {
                GlideRequest()
            }
            typePicasso -> {
                PicassoRequest()
            }
            else -> {
                GlideRequest()
            }
        }
    }

}

