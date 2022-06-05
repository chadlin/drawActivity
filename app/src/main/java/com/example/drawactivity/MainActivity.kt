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

	private lateinit var mCanvas: Canvas


	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
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