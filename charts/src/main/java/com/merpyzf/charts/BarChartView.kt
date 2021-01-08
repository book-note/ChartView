package com.merpyzf.barchart.ChartView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.merpyzf.barchart.ChartView.render.BarRenderer
import com.merpyzf.barchart.ChartView.render.BezierLinePathRenderer
import com.merpyzf.barchart.ChartView.render.XAxisRenderer
import com.merpyzf.charts.R


/**
 *
 * Description: 条形统计图
 * Date: 1/6/21
 * @author wangke
 *
 */
class BarChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // 图表的宽高比
    private val withHeightRatio = 1.4f

    // 图表内容左边界
    private var leftBorder = 0

    // 图表内容右边界
    private var rightBorder = 0

    // 柱状图的宽度
    private var barColumnWidth = 0f

    // 单位尺寸大小
    private var unitPixelSize = 0f

    // 记录上次点击的位置
    var lastX = 0f

    // 图表展示的数据集
    private val dataList = mutableListOf<Float>()
    private lateinit var bezierLinePathRenderer: BezierLinePathRenderer
    private val xAxisRenderer: XAxisRenderer = XAxisRenderer()
    private val barRenderer = BarRenderer()

    init {
        dashLineColor = ContextCompat.getColor(getContext(), R.color.black_34)
        barColor = ContextCompat.getColor(getContext(), R.color.teal_200)
        circleColor = Color.WHITE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var newWidth = 0
        var newHeight = 0
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            newWidth = MeasureSpec.getSize(widthMeasureSpec)
            newHeight = MeasureSpec.getSize(heightMeasureSpec)
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            newWidth = MeasureSpec.getSize(widthMeasureSpec)
            newHeight = (newWidth / withHeightRatio).toInt()
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            newWidth = MeasureSpec.getSize(widthMeasureSpec)
            newHeight = (newWidth / withHeightRatio).toInt()
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            newWidth = MeasureSpec.getSize(widthMeasureSpec)
            newHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        setMeasuredDimension(newWidth, newHeight)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = it.x
                }
                MotionEvent.ACTION_MOVE -> {
                    val movedX = lastX - it.x
                    when {
                        scrollX + movedX < leftBorder -> {
                            scrollTo(leftBorder, 0)
                        }
                        scrollX + movedX + width > rightBorder -> scrollTo(rightBorder - width, 0)
                        else -> {
                            scrollBy(movedX.toInt(), 0)
                        }
                    }
                    lastX = it.x
                }
            }
        }
        return true
    }

    fun setData(xAxisList: MutableList<String>, dataList: MutableList<Float>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        barRenderer.updateDataList(dataList)
        xAxisRenderer.updateAxisData(xAxisList)
        calculateBarColumnWidth()
        postInvalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        xAxisRenderer.setSize(width, height)
        calculateBarColumnWidth()
        calculateScrollBorder()
        bezierLinePathRenderer = BezierLinePathRenderer(rightBorder, height)
        barRenderer.setParams(
            width,
            height,
            xAxisRenderer.scaleSpace,
            xAxisRenderer.getTextHeight()
        )
    }

    /**
     * 计算图表左右可滑动的边界值
     */
    private fun calculateScrollBorder() {
        leftBorder = left
        rightBorder = (xAxisRenderer.scaleSpace * dataList.size).toInt()
    }

    private fun calculateBarColumnWidth() {
        barColumnWidth = width / XAxisRenderer.MAX_SCALE_COUNT / 2.5f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            bezierLinePathRenderer.updatePoints(getLinePoints(dataList))
            bezierLinePathRenderer.draw(it)
            xAxisRenderer.draw(it)
            barRenderer.draw(it)
        }
    }

    private fun getLinePoints(dataList: MutableList<Float>): MutableList<Point> {
        val linePoints = mutableListOf<Point>()
        for (i in 0 until dataList.size) {
            val circleRadius = barColumnWidth / 2
            linePoints.add(
                Point(
                    xAxisRenderer.scaleSpace / 2 + i * xAxisRenderer.scaleSpace,
                    height - (barRenderer.getUnitPixelSize() * dataList[i]) + barMarginTop + circleRadius
                )
            )
        }
        return linePoints
    }

    data class Point(var x: Float, var y: Float)
}