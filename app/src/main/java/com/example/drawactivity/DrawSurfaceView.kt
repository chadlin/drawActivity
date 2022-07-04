package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.*

class DrawSurfaceView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
	SurfaceView(context, attrs), SurfaceHolder.Callback, IDrawView {

	private val TAG = javaClass.simpleName
	private var statefulPaint: StatefulPaint = StatefulPaint()
	private lateinit var mThread: Thread
	private var flag: Boolean = false
	private var renderObject: RenderObject
	private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
	private var pen: Pen = Pen(statefulPaint, touchTolerance)
	private lateinit var list : MutableList<Stroke>
	private lateinit var bitmapHolder: BitmapHolder
	private lateinit var mBitmap: Bitmap
	private lateinit var mBitmapCanvas: Canvas

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
				refreshView()
			}
		}
		refreshView()
		return true
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun surfaceCreated(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceCreated========")
		if (flag) {
			mThread = Thread {
				while (true) {
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
	}

	override fun refreshView() {
		Log.d("XXXXX", "refreshView: refreshView")
		val canvas = holder.lockCanvas()
		bitmapHolder.drawBitmap(canvas)
		if (list.size > 0) {
			for (stroke in list) {
				canvas.drawPath(stroke.path, stroke.paint)
				mBitmapCanvas.drawPath(stroke.path, stroke.paint)
			}
		}
		holder.unlockCanvasAndPost(canvas)
	}

	override fun setBitmapHolder(bitmapHolder: BitmapHolder) {
		mBitmapCanvas = bitmapHolder.getCanvas()
		mBitmap = bitmapHolder.getBitmap()
		this.bitmapHolder = bitmapHolder
		Log.d(TAG, "setBitmapHolder: ${this.bitmapHolder}, $mBitmap, $mBitmapCanvas")
	}

	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
		Log.d("XXXXX", "=======surfaceChanged========")
	}

	override fun surfaceDestroyed(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceDestroyed========")
	}

}