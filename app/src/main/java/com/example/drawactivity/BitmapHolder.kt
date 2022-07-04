package com.example.drawactivity

import android.graphics.Bitmap
import android.graphics.Canvas

interface BitmapHolder {
	fun drawBitmap(canvas: Canvas)
	fun getBitmap(): Bitmap
	fun getCanvas(): Canvas
}