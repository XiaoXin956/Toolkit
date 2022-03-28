package com.xiaoxin.common.layout

import com.xiaoxin.common.R
import android.widget.FrameLayout
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.annotation.DrawableRes
import android.widget.TextView
import android.util.TypedValue
import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.lang.RuntimeException
import java.util.HashMap

/**
 * @author: Admin
 * @date: 2021-12-17
 */
class LoadingLayout constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.styleLoadingLayout) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var mEmptyImage: Int
    private var mEmptyText: CharSequence?
    private var mErrorImage: Int
    private var mErrorText: CharSequence?
    private var mRetryText: CharSequence?

    var mRetryButtonClickListener = OnClickListener { v ->
        if (mRetryListener != null) {
            mRetryListener!!.onClick(v)
        }
    }
    var mRetryListener: OnClickListener? = null
    private var mOnEmptyInflateListener: ((View) -> Unit)? = null
    private var mOnErrorInflateListener: ((View) -> Unit)? = null
    var mTextColor: Int
    var mTextSize: Int
    var mButtonTextColor: Int
    var mButtonTextSize: Int
    var mButtonBackground: Drawable?
    var mEmptyResId = NO_ID
    var mLoadingResId = NO_ID
    var mErrorResId = NO_ID
    var mContentId = NO_ID
    var mLayouts: MutableMap<Int, View> = HashMap()

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.styleLoadingLayout) {}

    private fun dp2px(dp: Float): Int {
        return (resources.displayMetrics.density * dp).toInt()
    }

    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 0) {
            return
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        val view = getChildAt(0)
        setContentView(view)
        showLoading()
    }

    private fun setContentView(view: View) {
        mContentId = view.id
        mLayouts[mContentId] = view
    }

    fun setLoading(@LayoutRes id: Int): LoadingLayout {
        if (mLoadingResId != id) {
            remove(mLoadingResId)
            mLoadingResId = id
        }
        return this
    }

    fun setEmpty(@LayoutRes id: Int): LoadingLayout {
        if (mEmptyResId != id) {
            remove(mEmptyResId)
            mEmptyResId = id
        }
        return this
    }

    fun setOnEmptyInflateListener(listener: ((View) -> Unit)?): LoadingLayout {
        mOnEmptyInflateListener = listener
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener?.invoke(mLayouts[mEmptyResId]!!)
        }
        return this
    }

    fun setOnErrorInflateListener(listener: ((View) -> Unit)?): LoadingLayout {
        mOnErrorInflateListener = listener
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener?.invoke(mLayouts[mErrorResId]!!)
        }
        return this
    }

    fun setEmptyImage(@DrawableRes resId: Int): LoadingLayout {
        mEmptyImage = resId
        image(mEmptyResId, R.id.empty_image, mEmptyImage)
        return this
    }

    fun setEmptyText(value: String?): LoadingLayout {
        mEmptyText = value
        text(mEmptyResId, R.id.empty_text, mEmptyText)
        return this
    }

    fun setErrorImage(@DrawableRes resId: Int): LoadingLayout {
        mErrorImage = resId
        image(mErrorResId, R.id.error_image, mErrorImage)
        return this
    }

    fun setErrorText(value: String?): LoadingLayout {
        mErrorText = value
        text(mErrorResId, R.id.error_text, mErrorText)
        return this
    }

    fun setRetryText(text: String?): LoadingLayout {
        mRetryText = text
        text(mErrorResId, R.id.retry_button, mRetryText)
        return this
    }

    fun setRetryListener(listener: OnClickListener?): LoadingLayout {
        mRetryListener = listener
        return this
    }

    fun showLoading() {
        show(mLoadingResId)
    }

    fun showEmpty() {
        show(mEmptyResId)
    }

    fun showError() {
        show(mErrorResId)
    }

    fun showContent() {
        show(mContentId)
    }

    private fun show(layoutId: Int) {
        for (view in mLayouts.values) {
            view.visibility = GONE
        }
        layout(layoutId)!!.visibility = VISIBLE
    }

    private fun remove(layoutId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val vg = mLayouts.remove(layoutId)
            removeView(vg)
        }
    }

    private fun layout(layoutId: Int): View? {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts[layoutId]
        }
        val layout = mInflater.inflate(layoutId, this, false)
        layout.visibility = GONE
        addView(layout)
        mLayouts[layoutId] = layout
        if (layoutId == mEmptyResId) {
            val img = layout.findViewById<View>(R.id.empty_image) as ImageView
            img.setImageResource(mEmptyImage)
            val view = layout.findViewById<View>(R.id.empty_text) as TextView
            view.let {
                it.text = mEmptyText
                it.setTextColor(mTextColor)
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener!!.invoke(layout)
            }
        } else if (layoutId == mErrorResId) {
            val img = layout.findViewById<View>(R.id.error_image) as ImageView
            img.setImageResource(mErrorImage)
            val txt = layout.findViewById<View>(R.id.error_text) as TextView
            txt.let {
                it.text = mErrorText
                it.setTextColor(mTextColor)
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }
            val btn = layout.findViewById<View>(R.id.retry_button) as TextView
            btn.let {
                it.text = mRetryText
                it.setTextColor(mButtonTextColor)
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize.toFloat())
                it.background = mButtonBackground
                it.setOnClickListener(mRetryButtonClickListener)
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener!!.invoke(layout)
            }
        }
        return layout
    }

    private fun text(layoutId: Int, ctrlId: Int, value: CharSequence?) {
        if (mLayouts.containsKey(layoutId)) {
            val view = mLayouts[layoutId]!!.findViewById<View>(ctrlId) as TextView
            view.text = value
        }
    }

    private fun image(layoutId: Int, ctrlId: Int, resId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val view = mLayouts[layoutId]!!.findViewById<View>(ctrlId) as ImageView
            view.setImageResource(resId)
        }
    }

    companion object {
        fun wrap(activity: Activity): LoadingLayout {
            return wrap((activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
        }

        fun wrap(fragment: Fragment): LoadingLayout {
            return wrap(fragment.view)
        }

        fun wrap(view: View?): LoadingLayout {
            if (view == null) {
                throw RuntimeException("content view can not be null")
            }
            val parent = view.parent as ViewGroup
            if (view == null) {
                throw RuntimeException("parent view can not be null")
            }
            val lp = view.layoutParams
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            val layout = LoadingLayout(view.context)
            parent.addView(layout, index, lp)
            layout.addView(view)
            layout.setContentView(view)
            return layout
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayout_Style)
        mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, NO_ID)
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText)
        mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, NO_ID)
        mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText)
        mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText)
        mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, -0x666667)
        mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, dp2px(16f))
        mButtonTextColor = a.getColor(R.styleable.LoadingLayout_llButtonTextColor, -0x666667)
        mButtonTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llButtonTextSize, dp2px(16f))
        mButtonBackground = a.getDrawable(R.styleable.LoadingLayout_llButtonBackground)
        mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout._loading_layout_empty)
        mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout._loading_layout_loading)
        mErrorResId = a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout._loading_layout_error)
        a.recycle()
    }
}