package com.example.drawactivity

import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent

class surfacePaint constructor(private val mPaint: Paint, private val touchTolerance: Int) : RenderObject() {

    private lateinit var curPath: Path
    private var currentX = 0f
    private var currentY = 0f

    override fun actionDown(event: MotionEvent) {
        curPath = Path()
        val curPaint = Paint(mPaint)
        strokeList.add(SmoothStroke(curPath, curPaint))
        curPath.reset()
        curPath.moveTo(event.x, event.y)
        currentX = event.x
        currentY = event.y
    }

    override fun actionMove(event: MotionEvent) {
        val dx = Math.abs(event.x - currentX)
        val dy = Math.abs(event.y - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            curPath.quadTo(currentX, currentY, (event.x + currentX) / 2, (event.y + currentY) / 2)
            currentX = event.x
            currentY = event.y
        }
    }

    override fun actionUp(event: MotionEvent) {
        curPath.lineTo(currentX, currentY)
    }
}