package com.example.drawactivity

import android.graphics.*
import com.example.drawactivity.PaintBoard2.DrawMode

class StatefulPaint: Paint() {

    private val PEN_STROKE_WIDTH: Int = DimenUtils.dp2pxInt(3f)
    private val HIGHLIGHTER_STROKE_WIDTH: Int = DimenUtils.dp2pxInt(10f)
    private val ERASER_STROKE_WIDTH: Int = DimenUtils.dp2pxInt(30f)
    private val XFERMODE_DRAW: Xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    private val XFERMODE_CLEAR: Xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private lateinit var currentMode: DrawMode

    init {
        setMode(DrawMode.DRAW)
    }

    fun setMode(drawMode: DrawMode){
        currentMode = drawMode
        when(drawMode){
            DrawMode.DRAW -> {
                color = Color.BLACK
                strokeWidth = PEN_STROKE_WIDTH.toFloat()
                xfermode = XFERMODE_DRAW
                alpha = 255
                style = Style.STROKE
            }
            DrawMode.HIGHLIGHTER -> {
                color = Color.YELLOW
                strokeWidth = HIGHLIGHTER_STROKE_WIDTH.toFloat()
                xfermode = XFERMODE_DRAW
                alpha = 150
                style = Style.STROKE
            }
            DrawMode.ERASER -> {
                xfermode = XFERMODE_CLEAR
                alpha = 255
                strokeWidth = ERASER_STROKE_WIDTH.toFloat()
                style = Style.STROKE
            }
        }
    }
}