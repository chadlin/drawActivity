package com.example.drawactivity

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log


class PaintPath(var path: Path, var paint: Paint) {

    fun draw(canvas: Canvas){
        Log.d("XXXXX", "$path, $paint")
        canvas.drawPath(path, paint)
    }

}