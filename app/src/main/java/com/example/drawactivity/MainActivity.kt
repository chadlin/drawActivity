package com.example.drawactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var paintBoard2 : PaintBoard2
    private lateinit var button: Button
    private lateinit var controlUndo: ImageView
    private lateinit var controlRedo: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paintBoard2 = findViewById(R.id.paint_board2)
        paintBoard2.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        controlUndo = findViewById(R.id.ControlUndo)
        controlRedo = findViewById(R.id.ControlRedo)

        controlUndo.setOnClickListener {
            paintBoard2.setUndo()
        }

        controlRedo.setOnClickListener {
            paintBoard2.setRedo()
        }
    }


    private val COLOR_MENU_ID = Menu.FIRST
    private val EMBOSS_MENU_ID = Menu.FIRST + 1
    private val BLUR_MENU_ID = Menu.FIRST + 2
    private val ERASE_MENU_ID = Menu.FIRST + 3
    private val SRCATOP_MENU_ID = Menu.FIRST + 4

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