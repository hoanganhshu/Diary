package com.diary.app.demo.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class DashedDividerDecoration(
    context: Context,
    private val marginHorizontalDp: Float = 16f
) : RecyclerView.ItemDecoration() {

    private val density = context.resources.displayMetrics.density
    private val marginPx = marginHorizontalDp * density

    private val paint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f * density
        pathEffect = DashPathEffect(floatArrayOf(12f * density, 8f * density), 0f)
        isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + marginPx
        val right = parent.width - parent.paddingRight - marginPx

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val y = child.bottom.toFloat()
            c.drawLine(left, y, right, y, paint)
        }
    }
}