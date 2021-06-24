package com.example.drawactivity

import android.graphics.Paint
import android.graphics.Path
import android.util.Log

class SmoothStroke constructor(override val path: Path, override val paint: Paint) : Stroke() {

    init {
        Log.d("XXXXX", "SmoothStroke:init")
    }
}