package com.merpyzf.barchart.ChartView.render

import android.graphics.*
import android.util.Log
import com.merpyzf.barchart.ChartView.BarChartView
import com.merpyzf.barchart.ChartView.canvasColor
import com.merpyzf.barchart.ChartView.dashLineColor
import com.merpyzf.barchart.ChartView.pathStrokeWidth

/**
 *
 * Description: 负责平滑折线绘制
 * Date: 1/8/21
 * @author wangke
 *
 */
class BezierLinePathRenderer(
    private val contentWidth: Int,
    private val contentHeight: Int,
) {
    private val points: MutableList<BarChartView.Point> = mutableListOf()
    private var bitmap: Bitmap =
        Bitmap.createBitmap(contentWidth, contentHeight, Bitmap.Config.RGB_565)
    private var paint: Paint = Paint()
    private var path: Path = Path()
    private var isDrawPath = false

    init {
        paint.isAntiAlias = true
        paint.color = dashLineColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = pathStrokeWidth
        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 15f), 0f)
        bitmap.eraseColor(canvasColor)
    }

    fun draw(canvas: Canvas) {
        for (i in 0 until points.size) {
            val scale = 0.11f
            var last1X: Float
            var last1Y: Float
            var last2X: Float
            var last2Y: Float
            var nowX: Float
            var nowY: Float
            var nextX: Float
            var nextY: Float
            var cAx: Float
            var cAy: Float
            var cBx: Float
            var cBy: Float
            if (i == 0) {
                path.moveTo(points[i].x, points[i].y)
            } else if (i == 1) {
                nowX = points[i].x
                nowY = points[i].y
                nextX = points[i + 1].x
                nextY = points[i + 1].y
                last1X = points[i - 1].x
                last1Y = points[i - 1].y
                cAx = (last1X + (nowX - 0) * scale)
                cAy = (last1Y + (nowY - contentHeight) * scale)
                cBx = (nowX - (nextX - last1X) * scale)
                cBy = (nowY - (nextY - last1Y) * scale)
                path.cubicTo(cAx, cAy, cBx, cBy, nowX, nowY)
            } else if (i == points.size - 1) {
                nowX = points[i].x
                nowY = points[i].y
                last1X = points[i - 1].x
                last1Y = points[i - 1].y
                last2X = points[i - 2].x
                last2Y = points[i - 2].y
                cAx = last1X + (nowX - last2X) * scale
                cAy = last1Y + (nowY - last2Y) * scale
                cBx = nowX - (nowX - last1X) * scale
                cBy = nowY - (nowY - last1Y) * scale
                path.cubicTo(cAx, cAy, cBx, cBy, nowX, nowY)
            } else {
                last1X = points[i - 1].x
                last1Y = points[i - 1].y
                last2X = points[i - 2].x
                last2Y = points[i - 2].y
                nowX = points[i].x
                nowY = points[i].y
                nextX = points[i + 1].x
                nextY = points[i + 1].y
                cAx = last1X + (nowX - last2X) * scale
                cAy = last1Y + (nowY - last2Y) * scale
                cBx = nowX - (nextX - last1X) * scale
                cBy = nowY - (nextY - last1Y) * scale
                path.cubicTo(
                    cAx, cAy, cBx,
                    cBy, nowX, nowY
                )
            }
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        if (!isDrawPath) {
            val bmpCanvas = Canvas(bitmap)
            bmpCanvas.drawPath(path, paint)
            isDrawPath = true
        }
    }

    fun updatePoints(points: MutableList<BarChartView.Point>) {
        this.points.clear()
        this.points.addAll(points)
    }
}