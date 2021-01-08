package com.merpyzf.chartview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.merpyzf.barchart.ChartView.BarChartView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val barChartView = findViewById<BarChartView>(R.id.barChartView)
        val barChartView1 = findViewById<BarChartView>(R.id.barChartView1)
        showMonthData(barChartView)
        showWeekData(barChartView1)
    }

    private fun showMonthData(barChartView: BarChartView?) {
        barChartView?.let {
            val dataList = mutableListOf<Float>()
            val xAxisList = mutableListOf<String>()
            for (i in 0 until 31) {
                dataList.add((10..50).random().toFloat())
                xAxisList.add("${i + 1}日")
            }
            dataList[29] = 0f
            dataList[30] = 0f
            it.setData(xAxisList, dataList)
        }
    }

    private fun showWeekData(barChartView: BarChartView?) {
        barChartView?.let {
            val dataList = mutableListOf<Float>()
            val xAxisList = mutableListOf<String>("周一", "周二", "周三", "周四", "周五", "周六", "周日")
            for (i in 0 until 7) {
                dataList.add((10..50).random().toFloat())
            }
            it.setData(xAxisList, dataList)
        }
    }
}