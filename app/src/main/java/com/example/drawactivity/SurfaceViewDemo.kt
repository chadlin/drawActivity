package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.*

class SurfaceViewDemo @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
	SurfaceView(context, attrs), SurfaceHolder.Callback, IDrawView {

	private var statefulPaint: StatefulPaint = StatefulPaint()
	private lateinit var mCanvas: Canvas
	private lateinit var mThread: Thread
	private var flag: Boolean = false
	private var renderObject: RenderObject
	private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
	private var pen: Pen = Pen(statefulPaint, touchTolerance)
	private lateinit var list : MutableList<Stroke>

	init {
		Log.d("XXXXX", "init:")
		setZOrderOnTop(true)
		holder.setFormat(PixelFormat.TRANSPARENT)
		holder.addCallback(this)
		statefulPaint = StatefulPaint()
		renderObject = pen
		copyStrokeList()
	}

	private fun copyStrokeList() {
		list = renderObject.strokeList.toMutableList()
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean {
		Log.d("XXXXX", "onTouchEvent: ${event.action}")
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				renderObject.actionDown(event)
				copyStrokeList()
			}
			MotionEvent.ACTION_MOVE -> {
				renderObject.actionMove(event)
			}
			MotionEvent.ACTION_UP -> {
				renderObject.actionUp(event)
			}
		}
		return true
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun surfaceCreated(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceCreated========")
		flag = true
		mThread = Thread {
			while (flag) {
				try {
					synchronized(holder) {
						//Thread.sleep(100)
						refreshView()
					}
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}
			}
		}
		mThread.start()
	}

	override fun refreshView() {
		//Log.d("XXXXX", "refreshView: refreshView")
		mCanvas = holder.lockCanvas()
		if (list.size > 0) {
			for (stroke in list) {
				mCanvas.drawPath(stroke.path, stroke.paint)
			}
		}
		holder.unlockCanvasAndPost(mCanvas)
	}

	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		Log.d("XXXXX", "=======surfaceChanged========")
	}

	override fun surfaceDestroyed(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceDestroyed========")
		flag = false
	}
}