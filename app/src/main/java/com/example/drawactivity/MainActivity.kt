package com.example.drawactivity

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

	private lateinit var paintBoard2: PaintBoard2
	private lateinit var surfaceViewDemo: SurfaceViewDemo
	private lateinit var button: Button
	private lateinit var controlUndo: ImageView
	private lateinit var controlRedo: ImageView
	private lateinit var mSurfaceView: SurfaceView

	private lateinit var mCanvas: Canvas


	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mSurfaceView = findViewById(R.id.surfaceView)
		paintBoard2 = findViewById(R.id.paint_board2)
		paintBoard2.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
		surfaceViewDemo = findViewById(R.id.surfaceViewDemo)
		controlUndo = findViewById(R.id.ControlUndo)
		controlRedo = findViewById(R.id.ControlRedo)


		controlUndo.setOnClickListener {
			paintBoard2.setUndo()
		}

		controlRedo.setOnClickListener {
			paintBoard2.setRedo()
		}

		//mSurfaceView.alpha = 0.8f
		mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
			override fun surfaceCreated(p0: SurfaceHolder) {
				Log.d("XXXXX", "=======surfaceCreated========")
				Thread {
					Log.d("XXXXX", "=======drawThread========")
					draw()
				}.start()
			}

			override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
				Log.d("XXXXX", "=======surfaceChanged========")
			}

			override fun surfaceDestroyed(p0: SurfaceHolder) {
				Log.d("XXXXX", "=======surfaceDestroyed========")
			}

		})

		mSurfaceView.setOnTouchListener { _, event ->
			Log.d("XXXXX", "mSurfaceView: ${event.action}")
			when (event?.action) {
				MotionEvent.ACTION_DOWN -> {
					Log.d("XXXXX", "mSurfaceView::ACTION_DOWN")
					//renderObject.actionDown(event)
				}
				MotionEvent.ACTION_MOVE -> {
					Log.d("XXXXX", "mSurfaceView::ACTION_MOVE")
					//renderObject.actionMove(event)
				}
				MotionEvent.ACTION_UP -> {
					Log.d("XXXXX", "mSurfaceView::ACTION_UP")
					//renderObject.actionUp(event)
				}
			}
			true
		}
	}


	private fun draw() {
		try {
			Log.d("XXXXX", "=======draw========")
			mCanvas = mSurfaceView.holder.lockCanvas()
			mCanvas.drawColor(Color.RED)
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			mSurfaceView.holder.unlockCanvasAndPost(mCanvas)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		val inflater: MenuInflater = menuInflater
		inflater.inflate(R.menu.option_menu, menu)
		return true
	}

	override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
		return super.onPrepareOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle item selection
		return when (item.itemId) {
			R.id.draw -> {
				paintBoard2.setPenMode()
				true
			}
			R.id.eraser -> {
				paintBoard2.setEraserMode()
				true
			}
			R.id.changeColor -> {
				paintBoard2.setHighliterMode()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

}