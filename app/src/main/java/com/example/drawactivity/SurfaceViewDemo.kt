package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

class SurfaceViewDemo @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
	SurfaceView(context, attrs), SurfaceHolder.Callback {

	private val TAG = javaClass.simpleName
	private var statefulPaint: StatefulPaint = StatefulPaint()
	private val backgroundColor =
		ResourcesCompat.getColor(resources, R.color.colorBackgroundSurfaceView, null)
	private lateinit var mBitmap: Bitmap
	private lateinit var mBitmapCanvas: Canvas
	private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
	private var undoneStrokeList = ArrayList<Stroke>()

	private var pen: Pen = Pen(statefulPaint, touchTolerance)

	private lateinit var mCanvas: Canvas


	init {
		Log.d("XXXXX", "init:")
		holder.addCallback(this)
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean {
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				pen.actionDown(event)
			}
			MotionEvent.ACTION_MOVE -> {
				pen.actionMove(event)
			}
			MotionEvent.ACTION_UP -> {
				pen.actionUp(event)
			}
		}
		return true
	}

	override fun surfaceCreated(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceCreated========")
	}

	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		Log.d("XXXXX", "=======surfaceChanged========")
	}

	override fun surfaceDestroyed(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceDestroyed========")
	}
}