package com.example.drawactivity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.*
import java.io.IOException

class SurfaceViewDemo @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
	SurfaceView(context, attrs), SurfaceHolder.Callback {

	private var paint: Paint
	private lateinit var path: Path
	private lateinit var mCanvas: Canvas
	private lateinit var mThread: Thread
	private var flag: Boolean = false
	private var mediaPlayer: MediaPlayer

	init {
		Log.d("XXXXX", "init:")
		setZOrderOnTop(true)
		holder.setFormat(PixelFormat.TRANSPARENT)
		holder.addCallback(this)
		paint = Paint()
		paint.apply {
			color = Color.YELLOW
			isAntiAlias = true
			isDither = true
			style = Paint.Style.STROKE
			strokeJoin = Paint.Join.ROUND
			strokeCap = Paint.Cap.ROUND
			strokeWidth = 30F
		}

		val uri =
			"https://jie-storage-test.s3-accelerate.amazonaws.com/dm_media/ce191c40-b29c-4258-a34a-ca910a648d59/Wash_Your_Hands.mp4"
		mediaPlayer = MediaPlayer()
		try {
			if (context != null) {
				mediaPlayer.setDataSource(context, Uri.parse(uri))
			}
			mediaPlayer.setOnPreparedListener {
				Log.d("XXXXX", "setOnPreparedListener")
				it.start()
				it.isLooping = true
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean {
		Log.d("XXXXX", "onTouchEvent: ${event.action}")
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
				path.lineTo(eventX, eventY)
//				mCanvas = holder.lockCanvas()
//				mCanvas.drawPath(path, paint)
//				holder.unlockCanvasAndPost(mCanvas)
			}
		}
//		mCanvas = holder.lockCanvas()
//		mCanvas.drawPath(path, paint)
//		holder.unlockCanvasAndPost(mCanvas)
		return true
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun surfaceCreated(holder: SurfaceHolder) {
		Log.d("XXXXX", "=======surfaceCreated========")
		flag = true
		path = Path()
//		mediaPlayer.setDisplay(holder)
//		mediaPlayer.prepare()


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

	private fun refreshView() {
		Log.d("XXXXX", "refreshView: refreshView")
		mCanvas = holder.lockCanvas()
		mCanvas.drawPath(path, paint)
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