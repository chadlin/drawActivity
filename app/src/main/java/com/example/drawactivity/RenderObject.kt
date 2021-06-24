package com.example.drawactivity

import android.view.MotionEvent

abstract class RenderObject {

    var strokeList: ArrayList<Stroke> = ArrayList()

    abstract fun actionDown(event: MotionEvent)

    abstract fun actionMove(event: MotionEvent)

    abstract fun actionUp(event: MotionEvent)

}