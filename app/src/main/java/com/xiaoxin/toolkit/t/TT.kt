package com.xiaoxin.toolkit.t

import android.content.Context
import com.bumptech.glide.Glide

/**
 * @author: Admin
 * @date: 2022-04-13
 */
interface IImageLoad {

    fun load(context: Context, url: String)

}

class GlideLoad : IImageLoad {
    override fun load(context: Context, url: String) {}
}

class PicLoad : IImageLoad {
    override fun load(context: Context, url: String) {}
}

object ImageAbs {

    val reqGlideType = "glide"
    val reqPicType = "pic"

    fun reqLoad(type: String):IImageLoad? {
         when (type) {
            reqGlideType -> {
                return GlideLoad()
            }
            reqPicType -> {
                return PicLoad()
            }
        }
        return null
    }

}