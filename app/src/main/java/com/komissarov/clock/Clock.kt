package com.komissarov.clock


import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Clock : View {
    private var clockHeight = 0
    private var clockWidth = 0
    private var fontSize = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint: Paint = Paint()
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initClock() {
        clockHeight = height
        clockWidth = width
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13f,
            resources.displayMetrics
        ).toInt()
        val min = clockHeight.coerceAtMost(clockWidth)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 6
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine(
            (clockWidth / 2).toFloat(),
            (clockHeight / 2).toFloat(),
            (clockWidth / 2 + cos(angle) * handRadius).toFloat(),
            (clockHeight / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c[Calendar.HOUR_OF_DAY].toFloat()
        hour %= 12
        drawHand(canvas, ((hour + c[Calendar.MINUTE] / 60) * 5f).toDouble(), true)
        drawHand(canvas, c[Calendar.MINUTE].toDouble(), false)
        drawHand(canvas, c[Calendar.SECOND].toDouble(), false)
    }

    private fun drawNumeral(canvas: Canvas) {
        paint.textSize = fontSize.toFloat()
        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (clockWidth / 2 + cos(angle) * radius - rect.width() / 2).toInt()
            val y = (clockHeight / 2 + sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        canvas.drawCircle((clockWidth / 2).toFloat(), (clockHeight / 2).toFloat(), 12f, paint)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset()
        paint.color = resources.getColor(R.color.black)
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas.drawCircle(
            (clockWidth / 2).toFloat(),
            (clockHeight / 2).toFloat(),
            (radius + padding - 10).toFloat(),
            paint
        )
    }

    companion object {

        const val padding: Int = 50
    }
}