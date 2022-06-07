package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

class PaintBoard2 @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val TAG = javaClass.simpleName
    private var statefulPaint: StatefulPaint = StatefulPaint()
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private lateinit var mBitmap: Bitmap
    private lateinit var mBitmapCanvas: Canvas
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private var undoneStrokeList = ArrayList<Stroke>()

    private var renderObject:RenderObject
    private var pen:Pen

    enum class DrawMode {
        DRAW, HIGHLIGHTER, ERASER, PALM_ERASER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::mBitmap.isInitialized) mBitmap.recycle()
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mBitmapCanvas = Canvas(mBitmap)
        //mBitmapCanvas.drawColor(backgroundColor)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderObject.actionDown(event)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                renderObject.actionMove(event)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                renderObject.actionUp(event)
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap, 0f, 0f, null)
        if (renderObject.strokeList.size > 0) {
            for (stroke in renderObject.strokeList) {
                canvas.drawPath(stroke.path, stroke.paint)
            }
        }
    }

    fun setUndo() {
        val size = renderObject.strokeList.size
        if (size > 0) {
            undoneStrokeList.add(renderObject.strokeList[size - 1])
            renderObject.strokeList.removeAt(size - 1)
            invalidate()
        }
    }

    fun setRedo() {
        val size = undoneStrokeList.size
        if (size > 0) {
            renderObject.strokeList.add(undoneStrokeList[size - 1])
            undoneStrokeList.removeAt(size - 1)
            invalidate()
        }
    }


    init {
        pen = Pen(statefulPaint, touchTolerance)
        renderObject = pen
    }

    fun setPenMode(){
        statefulPaint.setMode(DrawMode.DRAW)
    }

    fun setEraserMode(){
        statefulPaint.setMode(DrawMode.ERASER)
    }

    fun setHighliterMode(){
        statefulPaint.setMode(DrawMode.HIGHLIGHTER)
    }


}