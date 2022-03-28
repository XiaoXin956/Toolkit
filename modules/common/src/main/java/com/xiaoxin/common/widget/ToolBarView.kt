package com.xiaoxin.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

/**
 * @author: Admin
 * @date: 2022-03-08
 */
class ToolBarView : Toolbar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun findView(id: Int): View? {
        val childCount = this.childCount
        for (index in 0..childCount) {
            val view = getChildAt(index)
            if (id == view.id) {
                return view
            }
        }
        return null
    }

    fun <T : View> addViews(view: T, index: Int) {
        this.addChildView(view, index)
    }

    /**
     * 添加单个
     * @param leftView T
     */
    fun <T : View> addLeftMenu(leftView: T) {
        this.addChildView(leftView, Gravity.START)
    }

    /**
     * 添加多个view
     * @param leftViews T
     */
    fun <T : List<View>> addLeftMenus(leftViews: T) {
        leftViews.forEach() { leftView ->
            this.addChildView(leftView, Gravity.START)
        }
    }

    fun <T : View> addCenter(centerView: T) {
        title = ""
        centerView.let {
            this.addChildView(centerView, Gravity.CENTER)
        }
    }

    /**
     * 添加单个
     * @param leftView T
     */
    fun <T : View> addRightMenu(leftView: T) {
        this.addChildView(leftView, Gravity.END)
    }

    /**
     * 添加多个view
     * @param leftViews T
     */
    fun <T : List<View>> addRightMenus(leftViews: T) {
        leftViews.forEach() { leftView ->
            this.addChildView(leftView, Gravity.END)
        }
    }

    fun addChildView(view: View, index: Int) {
        addChildView(view, index, 0, 0, 0, 0)
    }

    private fun addChildView(v: View, gravity: Int, left: Int, top: Int, right: Int, bottom: Int) {
        val lp = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, gravity
        )
        lp.setMargins(left, top, right, bottom)
        this.addView(v, lp)
    }


}