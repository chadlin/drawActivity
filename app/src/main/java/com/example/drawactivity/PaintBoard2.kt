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
    private val mPaint: Paint
    //private var mPath : Path = Path()
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    //private lateinit var mBitmap: Bitmap
    //private lateinit var mBitmapCanvas: Canvas
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame: Rect

    // Path representing what's currently being drawn
    private var curPath : Path? = null
    private var pathList = ArrayList<PaintPath>()
    private var undonePathList = ArrayList<PaintPath>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //if (::mBitmap.isInitialized) mBitmap.recycle()
        //mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //mBitmapCanvas = Canvas(mBitmap)
        //mBitmapCanvas.drawColor(backgroundColor)
        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {
        curPath = Path()
        val paintPath = PaintPath(curPath!!)
        pathList.add(paintPath)
        curPath!!.reset()
        curPath!!.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
        invalidate()
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            curPath!!.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            //mBitmapCanvas.drawPath(mPath, mPaint)
        }
        invalidate()
    }

    private fun touchUp() {
        curPath!!.lineTo(currentX, currentY)
        // Rewind the current path for the next touch
        //curPath.reset()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if(pathList.size > 0){
            for (path in pathList){
                canvas.drawPath(path.path, mPaint)
            }
        }
        // Draw a frame around the canvas
        canvas.drawRect(frame, mPaint)
    }

    fun setUndo(){
        val size = pathList.size
        if(size >0){
            undonePathList.add(pathList[size-1])
            pathList.removeAt(size -1)
            invalidate()
        }
    }

    fun setRedo(){
        val size = undonePathList.size
        if(size >0){
            pathList.add(undonePathList[size -1])
            undonePathList.removeAt(size -1)
            invalidate()
        }
    }


    init {
        mPaint = Paint().apply {
            color = drawColor
            // Smooths out edges of what is drawn without affecting shape.
            isAntiAlias = true
            // Dithering affects how colors with higher-precision than the device are down-sampled.
            isDither = true
            style = Paint.Style.STROKE // default: FILL
            strokeJoin = Paint.Join.ROUND // default: MITER
            strokeCap = Paint.Cap.ROUND // default: BUTT
            strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
        }
    }

    companion object {
        private const val STROKE_WIDTH = 12f // has to be float
    }
}