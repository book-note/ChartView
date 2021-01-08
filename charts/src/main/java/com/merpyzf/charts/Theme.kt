package com.merpyzf.barchart.ChartView

import android.graphics.Color

/**
 *
 * Description: 图表主题管理
 * Date: 1/8/21
 * @author wangke
 *
 */
data class Theme(
    val isDark: Boolean,
    val nightCanvasBg: Int,
    val lightCanvasBg: Int,
) {

    fun getCanvasBg(): Int {
        return if (isDark) {
            nightCanvasBg
        } else {
            lightCanvasBg
        }
    }
}