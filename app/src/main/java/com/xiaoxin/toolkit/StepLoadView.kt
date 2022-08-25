package com.xiaoxin.toolkit

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * @author: Admin
 * @date: 2022-04-06
 */
class StepLoadView : View {

    var widthSize = 0
    var heightSize = 0

    val outerColor = Color.RED
    val innerColor = Color.BLUE

    val borderWidth = 20f
    var stepTextSize: Float = 16f
    var stepTextColor = Color.YELLOW
    lateinit var outPaint: Paint
    lateinit var innerPaint: Paint
    lateinit var textPaint: Paint
    var mStepMax = 5000
    var mCurrentStep = 220


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun init() {

        outPaint = Paint()
        outPaint.isAntiAlias = true
        outPaint.strokeWidth = borderWidth
        outPaint.color = outerColor
        outPaint.strokeCap = Paint.Cap.ROUND
        outPaint.style = Paint.Style.STROKE

        innerPaint = Paint(outPaint)
        innerPaint.isAntiAlias = true
        innerPaint.strokeWidth = borderWidth
        innerPaint.color = innerColor
        innerPaint.strokeCap = Paint.Cap.ROUND
        innerPaint.style = Paint.Style.STROKE

        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.textSize = 16f
        textPaint.color = Color.GREEN


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 宽高模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //宽高大小
        widthSize = MeasureSpec.getSize(widthMeasureSpec)
        heightSize = MeasureSpec.getSize(heightMeasureSpec)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e("自定义", "按下")
            }
            MotionEvent.ACTION_UP -> {
                Log.e("自定义", "抬起")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e("自定义", "移动")
            }
            else -> {}
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 画弧
        var center: Float = widthSize / 2F
        var radius: Float = widthSize / 2 - borderWidth / 2
        var rect = RectF(center - radius, center - radius, center + radius, center + radius)
        canvas?.drawArc(rect, 135F, 270F, false, outPaint)

        //绘制内
        var sweepAngle: Float = mCurrentStep.toFloat() / mStepMax.toFloat()
        canvas?.drawArc(rect, 135f, sweepAngle * 270f, false, innerPaint)


    }

    fun setMax(max: Int) {
        mStepMax = max
    }

    fun setCurrentStep(currentStep: Int) {
        mCurrentStep = currentStep
        invalidate()
    }



}