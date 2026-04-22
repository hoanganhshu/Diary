package com.diary.app.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CurvedBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val path = Path()

    // ==== THAM SỐ CÓ THỂ CHỈNH ====
    // Độ rộng notch (tỉ lệ theo bề ngang)
    private var notchSpanRatio = 0.36f
    // Độ sâu notch (tỉ lệ theo chiều cao)
    private var depthRatio = 0.65f
    // Độ lùi control point (tỉ lệ theo notch span)
    private var cpInsetRatio = 0.3f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = resources.displayMetrics.density
        val desiredW = (360f * d).toInt()
        val desiredH = (72f * d).toInt()
        val w = resolveSize(desiredW, widthMeasureSpec)
        val h = resolveSize(desiredH, heightMeasureSpec)
        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val W = w.toFloat()
        val H = h.toFloat()
        val top = 0f

        val centerX = W / 2f
        val notchSpan = W * notchSpanRatio
        val depth = H * depthRatio
        val leftNotch = centerX - notchSpan / 2f
        val rightNotch = centerX + notchSpan / 2f
        val cpInsetX = notchSpan * cpInsetRatio

        path.reset()

        // ===== Bắt đầu từ góc trái (đường thẳng luôn) =====
        path.moveTo(0f, top)

        // Đường thẳng đến trước notch
        path.lineTo(leftNotch, top)

        // ===== Notch giữa =====
        path.cubicTo(
            leftNotch + cpInsetX, top,          // CP1
            centerX - cpInsetX, depth,          // CP2
            centerX, depth                      // Đáy notch
        )
        path.cubicTo(
            centerX + cpInsetX, depth,          // CP3
            rightNotch - cpInsetX, top,         // CP4
            rightNotch, top                     // Kết thúc notch
        )

        // Đường thẳng đến mép phải (bỏ bo)
        path.lineTo(W, top)

        // Khép phần còn lại
        path.lineTo(W, H)
        path.lineTo(0f, H)
        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    fun setShape(
        notchSpanRatio: Float? = null,
        depthRatio: Float? = null,
        cpInsetRatio: Float? = null
    ) {
        notchSpanRatio?.let { this.notchSpanRatio = it }
        depthRatio?.let { this.depthRatio = it }
        cpInsetRatio?.let { this.cpInsetRatio = it }
        requestLayout()
        invalidate()
    }
}
