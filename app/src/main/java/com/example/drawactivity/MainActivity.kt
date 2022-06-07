package com.example.drawactivity

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

	private lateinit var paintBoard2: PaintBoard2
	private lateinit var surfaceViewDemo: SurfaceViewDemo
	private lateinit var controlUndo: ImageView
	private lateinit var controlRedo: ImageView
	private lateinit var btnChangeColor: Button
	private val colors = intArrayOf(Color.WHITE, Color.GREEN, Color.MAGENTA, Color.BLUE)
	private var currentSurfaceBackgroundColor = Color.WHITE

	private lateinit var mCanvas: Canvas
	private lateinit var videoView: VideoView
	private lateinit var playButtonClickHandler: Button
	private lateinit var pauseButtonClickHandler: Button
	private lateinit var stopButtonClickHandler: Button


	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		paintBoard2 = findViewById(R.id.paint_board2)
		paintBoard2.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
		surfaceViewDemo = findViewById(R.id.surfaceViewDemo)
		videoView = findViewById(R.id.videoView)
		btnChangeColor = findViewById(R.id.changeColor)
		controlUndo = findViewById(R.id.ControlUndo)
		controlRedo = findViewById(R.id.ControlRedo)

		btnChangeColor.setOnClickListener {
			val colorIndex = Random.nextInt(colors.size)
			currentSurfaceBackgroundColor = colors[colorIndex]
			changeSurfaceBackgroundColor(currentSurfaceBackgroundColor);
		}

		controlUndo.setOnClickListener {
			paintBoard2.setUndo()
		}

		controlRedo.setOnClickListener {
			paintBoard2.setRedo()
		}
		setupVideoView()
	}

	private fun setupVideoView() {
		val uri =
			"https://jie-storage-test.s3-accelerate.amazonaws.com/dm_media/ce191c40-b29c-4258-a34a-ca910a648d59/Wash_Your_Hands.mp4"
		videoView = findViewById(R.id.videoView)
		playButtonClickHandler = findViewById(R.id.playButtonClickHandler)
		pauseButtonClickHandler = findViewById(R.id.pauseButtonClickHandler)
		stopButtonClickHandler = findViewById(R.id.stopButtonClickHandler)
		videoView.setVideoURI(Uri.parse(uri))
		// hide medie controller
		videoView.setMediaController(null)

		playButtonClickHandler.setOnClickListener {
			videoView.start()
		}

		pauseButtonClickHandler.setOnClickListener {
			videoView.pause()
		}

		stopButtonClickHandler.setOnClickListener {
			videoView.seekTo(0)
			videoView.pause()
		}
	}

	private fun changeSurfaceBackgroundColor(color: Int){
		paintBoard2.setBackgroundColor(color)
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