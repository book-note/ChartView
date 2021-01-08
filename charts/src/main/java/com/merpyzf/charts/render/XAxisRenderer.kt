package com.merpyzf.barchart.ChartView.render

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.merpyzf.barchart.ChartView.axisTextSize
import kotlin.math.abs

/**
 *
 * Description: 负责 X 坐标轴内容的绘制
 * Date: 1/8/21
 * @author wangke
 *
 */
class XAxisRenderer {
    private var contentWidth = 0
    private var contentHeight = 0
    var scaleCount = 0
    var scaleSpace = 0f
    private val axisDataList = mutableListOf<String>()
    private val axisPaint: Paint = Paint()


    companion object {
        var MAX_SCALE_COUNT = 10
    }

    init {
        axisPaint.isAntiAlias = true
        axisPaint.color = Color.GRAY
        axisPaint.textSize = axisTextSize
        axisPaint.textAlign = Paint.Align.CENTER
        calculateScaleCountAndSpace();
    }

    fun setSize(width: Int, height: Int){
        this.contentWidth = width
        this.contentHeight = height
        calculateScaleCountAndSpace();
    }

    fun updateAxisData(axisDataList: MutableList<String>) {
        if (axisDataList.size != 0) {
            this.axisDataList.clear()
            this.axisDataList.addAll(axisDataList)
            calculateScaleCountAndSpace()
        }
    }

    private fun calculateScaleCountAndSpace() {
        scaleCount = if (axisDataList.size < MAX_SCALE_COUNT) {
            axisDataList.size
        } else {
            MAX_SCALE_COUNT
        }
        if (scaleCount != 0) {
            scaleSpace = contentWidth * 1.0f / scaleCount
        }
    }

    fun getTextHeight(): Float {
        val fontMetrics = axisPaint.fontMetrics
        return abs(fontMetrics.top) + fontMetrics.bottom
    }

    fun draw(canvas: Canvas) {
        axisPaint.textSize = axisTextSize
        for (i in 0 until axisDataList.size) {
            val x = scaleSpace / 2 + i * scaleSpace
            // 绘制横坐标刻度信息
            canvas.drawText(
                axisDataList[i],
                x,
                contentHeight.toFloat() - axisPaint.fontMetrics.bottom,
                axisPaint
            )
        }
    }
}
