package com.merpyzf.barchart.ChartView.render

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import com.merpyzf.barchart.ChartView.barColor
import com.merpyzf.barchart.ChartView.barMarginBottom
import com.merpyzf.barchart.ChartView.barMarginTop
import com.merpyzf.barchart.ChartView.circleColor

/**
 *
 * Description:
 * Date: 1/8/21
 * @author wangke
 *
 */
class BarRenderer() {
    val dataList = mutableListOf<Float>()
    private var maxData: Float? = null
    private val paint = Paint()
    private val barRectF = RectF()
    private val circlePoint = Point()
    private var barColumnWidth = 0f
    private var unitPixelSize = 0f

    private var scaleSpace = 0f
    private var height = 0
    private var width = 0
    private var xAxisTextHeight = 0f

    init {
        paint.isAntiAlias = true
        paint.color = barColor
    }

    fun setParams(width: Int, height: Int, scaleSpace: Float, textHeight: Float) {
        setChartViewSize(width, height)
        setScaleSpace(scaleSpace)
        setXAxisTextHeight(textHeight)
    }

    fun setScaleSpace(scaleSpace: Float) {
        this.scaleSpace = scaleSpace
    }

    fun setChartViewSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun setXAxisTextHeight(textHeight: Float) {
        this.xAxisTextHeight = textHeight
    }

    fun updateDataList(dataList: MutableList<Float>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        this.maxData = dataList.maxOrNull()
    }

    fun getUnitPixelSize(): Float {
        if (maxData == null) {
            return 0f
        } else {
            return (height - barMarginBottom - barMarginTop - xAxisTextHeight) / maxData!!
        }
    }


    fun draw(canvas: Canvas) {
        barColumnWidth = width / XAxisRenderer.MAX_SCALE_COUNT / 2.5f
        unitPixelSize = getUnitPixelSize()
        for (i in 0 until dataList.size) {
            val x = scaleSpace / 2 + i * scaleSpace
            // 绘制条形图
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.color = barColor
            paint.strokeWidth = 0f
            barRectF.left = x - barColumnWidth / 2
            barRectF.top = height - (unitPixelSize * dataList[i]) + barMarginTop
            barRectF.right = x + barColumnWidth / 2
            barRectF.bottom = height - xAxisTextHeight - barMarginBottom - 20f
            // 装饰圆的半径
            val circleRadius = barColumnWidth / 2f
            circlePoint.x = x.toInt()
            circlePoint.y = (barRectF.top + circleRadius).toInt()
            if (dataList[i].toInt() != 0) {
                canvas.drawRoundRect(
                    barRectF, barColumnWidth / 2, barColumnWidth / 2, paint
                )
                // 绘制条形图顶部用于装饰的圆圈
                paint.style = Paint.Style.STROKE
                paint.color = circleColor
                paint.strokeWidth = 4f
                canvas.drawCircle(
                    circlePoint.x.toFloat(),
                    circlePoint.y.toFloat(),
                    barColumnWidth / 2 - (barColumnWidth / 2 / 2),
                    paint
                )
            }
        }
    }
}