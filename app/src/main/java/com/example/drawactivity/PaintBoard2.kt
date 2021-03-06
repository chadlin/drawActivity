package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

class PaintBoard2 @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs), IDrawView {

    private val TAG = javaClass.simpleName
    private var statefulPaint: StatefulPaint = StatefulPaint()
    private lateinit var bitmapHolder: BitmapHolder
    private lateinit var mBitmap: Bitmap
    private lateinit var mBitmapCanvas: Canvas
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private var undoneStrokeList = ArrayList<Stroke>()

    private var renderObject:RenderObject
    private var pen:Pen = Pen(statefulPaint, touchTolerance)

    enum class DrawMode {
        DRAW, HIGHLIGHTER, ERASER, PALM_ERASER
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
        Log.d(TAG, "onDraw: $canvas")
        bitmapHolder.drawBitmap(canvas)
        if (renderObject.strokeList.size > 0) {
            for (stroke in renderObject.strokeList) {
                canvas.drawPath(stroke.path, stroke.paint)
                mBitmapCanvas.drawPath(stroke.path, stroke.paint)
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

    override fun refreshView() {
        Log.d(TAG, "refreshView: $this")
    }

    override fun setBitmapHolder(bitmapHolder: BitmapHolder) {
        mBitmapCanvas = bitmapHolder.getCanvas()
        mBitmap = bitmapHolder.getBitmap()
        this.bitmapHolder = bitmapHolder
        Log.d(TAG, "setBitmapHolder: ${this.bitmapHolder}, $mBitmap, $mBitmapCanvas")
    }


}