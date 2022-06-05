package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.*

class SurfaceViewDemo @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
	SurfaceView(context, attrs), SurfaceHolder.Callback {

	private var paint: Paint
	private lateinit var path: Path

	init {
		Log.d("XXXXX", "init:")
		setZOrderOnTop(true)
		holder.setFormat(PixelFormat.TRANSPARENT)
		holder.addCallback(this)
		paint = Paint()
		paint.apply {
			color = Color.BLACK
			isAntiAlias = true
			strokeWidth = 50F
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	override fun surfaceCreated(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceCreated========")
		path = Path()
		setOnTouchListener { _, event ->
			val eventX = event.x
			val eventY = event.y
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					path.reset()
					path.moveTo(eventX, eventY)
				}
				MotionEvent.ACTION_MOVE -> {
					path.lineTo(eventX, eventY)
				}
				MotionEvent.ACTION_UP -> {
					path.lineTo(event.x, event.y)
					val canvas1 = holder.lockCanvas()
					canvas1.drawPath(path, paint)
					holder.unlockCanvasAndPost(canvas1)
				}
			}
			val canvas = holder.lockCanvas()
			canvas.drawPath(path, paint)
			holder.unlockCanvasAndPost(canvas)
			true
		}
	}

	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		Log.d("XXXXX", "=======surfaceChanged========")
	}

	override fun surfaceDestroyed(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceDestroyed========")
	}
}