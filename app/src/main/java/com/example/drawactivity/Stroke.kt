package com.example.drawactivity

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log


abstract class Stroke {

    abstract val path: Path
    abstract val paint: Paint


}