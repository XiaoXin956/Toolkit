package com.xiaoxin.basic.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xiaoxin.basic.R
import com.xiaoxin.basic.view.ViewUtils

/**
 * 侧边字符索引
 */
class LetterSideBar : View {

    private var letters = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z", "#"
    )
    var currentTouchLetter = "A"
    private lateinit var mPaint: Paint
    var textSize: Int = 14
    var selectColor = Color.BLUE
    var unSelectColor = Color.BLACK
    // 平均确认每个文字的高度
    var itemHeight = 0

    /**
     * 数据源
     * @param letters Array<String>
     */
    fun setLetters(letters: Array<String>) {
        this.letters = letters
        invalidate()
    }

    private val fontMetricsInt by lazy { Paint.FontMetrics() }
    private var onTouchListener: ((String, Int, Boolean) -> Unit)? = null

    fun setTouchListener(onTouchListener: ((String, Int, Boolean) -> Unit)?) {
        this.onTouchListener = onTouchListener
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar)
        textSize = typeArray.getDimension(R.styleable.LetterSideBar_textSize, 14F).toInt()
        selectColor = typeArray.getColor(R.styleable.LetterSideBar_selectColor, Color.BLUE)
        unSelectColor = typeArray.getColor(R.styleable.LetterSideBar_unSelectColor, Color.BLUE)
        typeArray.recycle()
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.textSize = ViewUtils.sp2px(textSize)
        mPaint.color = unSelectColor

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureTextWidth = mPaint.measureText("A")
        val width = MeasureSpec.getSize(widthMeasureSpec) + paddingLeft + paddingRight + measureTextWidth.toInt()
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        itemHeight = (height - paddingTop - paddingBottom) / letters.size
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        letters.forEachIndexed { index, res ->
            val textWidth = mPaint.measureText(res)
            // 中心线
            val letterCenter = index * itemHeight + itemHeight / 2 + paddingTop
            val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
            val baseLine = letterCenter + dy
            val x = width / 2 - textWidth / 2
            if (res == currentTouchLetter) {
                mPaint.color = selectColor
            } else {
                mPaint.color = unSelectColor
            }
            canvas?.drawText(res, x, baseLine, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val currencyMoveY = event.y // 当前位置
                val itemHeight = (height - paddingTop - paddingBottom) / letters.size // 每个文字的高度
                var currentPosition: Int = (currencyMoveY / itemHeight).toInt()
                if (currentPosition < 0) {
                    currentPosition = 0
                }
                if (currentPosition > letters.size - 1) {
                    currentPosition = (letters.size - 1)
                }
                currentTouchLetter = letters[currentPosition]
                if (onTouchListener != null) {
                    onTouchListener?.invoke(currentTouchLetter, currentPosition, true)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (onTouchListener != null) {
                    onTouchListener?.invoke(currentTouchLetter, -1, true)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val currencyMoveY = event.y // 当前位置
                val itemHeight = (height - paddingTop - paddingBottom) / letters.size // 每个文字的高度
                var currentPosition: Int = (currencyMoveY / itemHeight).toInt()
                if (currentPosition < 0) {
                    currentPosition = 0
                }
                if (currentPosition > letters.size - 1) {
                    currentPosition = (letters.size - 1)
                }
                currentTouchLetter = letters[currentPosition]
                if (onTouchListener != null) {
                    onTouchListener?.invoke(currentTouchLetter, currentPosition, true)
                }
                invalidate()
            }
            else -> {}
        }
        return true
    }

    override fun onCheckIsTextEditor(): Boolean {
        return false
    }

}