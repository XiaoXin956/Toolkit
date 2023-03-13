package com.xiaoxin.basic.adapter

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SmartViewHolder(
    itemView: View,
    var listener: ((parent: AdapterView<*>?, view: View, position: Int, id: Long) -> Unit)
    ):RecyclerView.ViewHolder(itemView),View.OnClickListener {

    var mPosition:Int = -1

    init {
        itemView.setOnClickListener(this)
    }

    fun setPosition(position: Int){
        mPosition = position
    }

    override fun onClick(v: View) {
        listener.let {
            val position = adapterPosition
            if(position>0){
                it.invoke(null,v,position,itemId)
            }else{
                it.invoke(null,v,mPosition,itemId)
            }
        }
    }

    /**
     * 查找 view
     * @param viewId Int
     * @return T
     */
    private fun findViewById(viewId:Int):View{
        return if(viewId==0){
            itemView
        }else{
            itemView.findViewById(viewId)
        }
    }

    /**
     * 设置文本
     * @param viewId Int
     * @param charSequence Any
     * @return SmartViewHolder
     */
    fun setText(viewId: Int, charSequence: Any): SmartViewHolder {
        val view = findViewById(viewId)
        if (view is TextView) {
            if (charSequence is CharSequence) {
                view.text = charSequence
            } else if (charSequence is Int) {
                view.text = charSequence.toString()
            }
        }
        return this
    }

    /**
     * 设置文本颜色
     * @param viewId Int
     * @param colorId Int
     * @return SmartViewHolder
     */
    fun setTextColor(viewId: Int, colorId: Int): SmartViewHolder {
        val view = findViewById(viewId)
        if (view is TextView) {
            view.setTextColor(colorId)
        }
        return this
    }

    /**
     * 设置背景资源
     * @param viewId Int
     * @param drawableId Int
     * @return SmartViewHolder
     */
    fun setTextBGDrawable(viewId: Int, drawableId: Int): SmartViewHolder {
        val view = findViewById(viewId)
        if (view is TextView) {
            view.setBackgroundResource(drawableId)
        }
        return this
    }

    /**
     * 设置背景颜色
     * @param viewId Int
     * @param colorId Int
     * @return SmartViewHolder
     */
    fun setTextBGColor(viewId: Int, colorId: Int): SmartViewHolder {
        val view = findViewById(viewId)
        if (view is TextView) {
            view.setBackgroundColor(colorId)
        }
        return this
    }

    /**
     * 设置图片资源
     * @param viewId Int
     * @param imageId Int
     * @return SmartViewHolder
     */
    fun image(viewId: Int, imageId: Int): SmartViewHolder {
        val view = findViewById(viewId)
        if (view is ImageView) {
            view.setImageResource(imageId)
        }
        return this
    }

    /**
     * 显示状态
     *
     * @param viewId Int
     * @param visibility Int  View.GONE、View.VISIBLE、View.INVISIBLE
     * @return SmartViewHolder
     */
    fun showState(viewId: Int, visibility: Int): SmartViewHolder {
        val view = findViewById(viewId)
        view.visibility = visibility
        return this
    }

}