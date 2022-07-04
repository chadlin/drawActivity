package com.example.drawactivity

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class StateHolder(private val context: Context) : BitmapHolder {

	private lateinit var mCanvas: Canvas
	private lateinit var mBitmap: Bitmap


	fun onCreate() {
		mBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
		mCanvas = Canvas(mBitmap)
		mCanvas.drawColor(Color.BLUE)
	}

	fun onDestroy() {
		if (!mBitmap.isRecycled) {
			mBitmap.recycle()
		}
	}

	override fun drawBitmap(canvas: Canvas) {
		Log.d("XXXXX", "drawBitmap: ${mBitmap.isRecycled}")
		if (!mBitmap.isRecycled) {
			Log.d("XXXXX", "drawBitmap: $canvas")
			canvas.drawBitmap(mBitmap, 0F, 0F, null)
		}
	}

	override fun getBitmap(): Bitmap {
		return mBitmap
	}

	override fun getCanvas(): Canvas {
		return mCanvas
	}

	fun saveMediaToStorage() {
		//Generating a file name
		val filename = "${System.currentTimeMillis()}.jpg"

		//Output stream
		var fos: OutputStream? = null

		//For devices running android >= Q
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			//getting the contentResolver
			context.contentResolver?.also { resolver ->

				//Content resolver will process the contentvalues
				val contentValues = ContentValues().apply {

					//putting file information in content values
					put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
					put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
					put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
				}

				//Inserting the contentValues to contentResolver and getting the Uri
				val imageUri: Uri? =
					resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

				//Opening an outputstream with the Uri that we got
				fos = imageUri?.let { resolver.openOutputStream(it) }
			}
		} else {
			//These for devices running on android < Q
			//So I don't think an explanation is needed here
			val imagesDir =
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
			val image = File(imagesDir, filename)
			fos = FileOutputStream(image)
		}

		fos?.use {
			//Finally writing the bitmap to the output stream that we opened
			Log.d("XXXXX", "saveMediaToStorage: $mBitmap")
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
			Toast.makeText(context, "Saved to Photos", Toast.LENGTH_LONG).show()
		}
	}

}