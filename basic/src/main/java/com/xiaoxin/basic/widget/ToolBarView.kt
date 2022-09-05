package com.xiaoxin.basic.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.xiaoxin.basic.R
import com.xiaoxin.basic.view.ViewUtils

/**
 * @author: Admin
 * @date: 2022-03-08
 */
class ToolBarView : Toolbar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(attrs = attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs = attrs)
    }

    fun init(attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolBarView)
        val backgroundColor = typedArray.getColor(R.styleable.ToolBarView_backgroundColor,Color.WHITE)
        val textTitle = typedArray.getString(R.styleable.ToolBarView_centerTitle)
        val textColor = typedArray.getColor(R.styleable.ToolBarView_textColor, Color.WHITE)
        val leftImageColor =
            typedArray.getColor(R.styleable.ToolBarView_leftImageColor, Color.WHITE)
        val leftIcon = typedArray.getResourceId(
            R.styleable.ToolBarView_leftIcon,
            R.drawable.ic_baseline_arrow_white_24
        )
        typedArray.recycle()

        setDefaultTitle(textTitle.toString(), textColor = textColor)
        setDefaultLeftImage(imageColor = leftImageColor, leftIcon = leftIcon)
        this.setBackgroundColor(backgroundColor)
    }

    var textTitleDefault: TextView? = null
    fun setDefaultTitle(title: String, textColor: Int? = Color.BLACK) {
        if (textTitleDefault==null) {
            textTitleDefault = TextView(context)
        }
        textTitleDefault?.let {
            it.text = title
            it.setTextColor(textColor!!)
            addCenter(it)
        }
    }


    var leftTitleDefault: ImageView? = null
    fun setDefaultLeftImage(imageColor: Int? = Color.BLACK, leftIcon: Int): Unit {
        if (leftTitleDefault==null) {
            leftTitleDefault = ImageView(context)
        }
        leftTitleDefault?.let {
            it.setBackgroundResource(leftIcon)
            addLeftMenu(it)
        }
    }

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

    private fun addChildView(view: View, index: Int) {
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