package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.res.ResourcesCompat

class PaintBoard2 @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val TAG = javaClass.simpleName
    private val mPaint: Paint
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.teal_700, null)
    private val highLighterColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    private lateinit var mBitmap: Bitmap
    private lateinit var mBitmapCanvas: Canvas
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame: Rect

    private var undoneStrokeList = ArrayList<Stroke>()

    private var drawingMode: Mode = Mode.DRAW

    private var renderObject:RenderObject
    private var pen:Pen

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::mBitmap.isInitialized) mBitmap.recycle()
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mBitmapCanvas = Canvas(mBitmap)
        mBitmapCanvas.drawColor(backgroundColor)
        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
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

        pen = Pen(mPaint, touchTolerance)
        renderObject = pen
    }

    @SuppressLint("ResourceAsColor")
    fun setPaint(mode: Mode) {
        mPaint.xfermode = null
        mPaint.alpha = 0xFF
        when (mode) {
            Mode.DRAW -> {
                drawingMode = Mode.DRAW
                mPaint.xfermode = null
                Toast.makeText(context, "$drawingMode", LENGTH_SHORT).show()
                true
            }
            Mode.ERASER -> {
                drawingMode = Mode.ERASER
                Toast.makeText(context, "$drawingMode", LENGTH_SHORT).show()
                mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                true
            }
            Mode.HIGHLIGHTER -> {
                drawingMode = Mode.HIGHLIGHTER
                mPaint.xfermode = null
                mPaint.color = highLighterColor
                mPaint.alpha = 155
            }
            else -> Toast.makeText(context, "not exist", LENGTH_SHORT).show()

        }
    }

    companion object {
        private const val STROKE_WIDTH = 12f // has to be float

        // endregion
        enum class Mode {
            DRAW, HIGHLIGHTER, ERASER, PALM_ERASER
        }
    }
}