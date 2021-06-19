package com.example.drawactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    @Suppress("UNREACHABLE_CODE")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.option_menu, menu)
        menu?.add(0, COLOR_MENU_ID, 0, "Color")?.setShortcut('3', 'c');
        menu?.add(0, EMBOSS_MENU_ID, 0, "Emboss")?.setShortcut('4', 's');
        menu?.add(0, BLUR_MENU_ID, 0, "Blur")?.setShortcut('5', 'z');
        menu?.add(0, ERASE_MENU_ID, 0, "Erase")?.setShortcut('5', 'z');
        menu?.add(0, SRCATOP_MENU_ID, 0, "SrcATop")?.setShortcut('5', 'z');
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }

}