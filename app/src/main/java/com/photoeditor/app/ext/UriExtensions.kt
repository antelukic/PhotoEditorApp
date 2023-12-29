package com.photoeditor.app.ext

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException

fun Uri.getBitmap(cr: ContentResolver): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val inputStream = cr.openInputStream(this)
        bitmap = BitmapFactory.decodeStream(inputStream)

        try {
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    } catch (e: FileNotFoundException) {
        Log.e("UriExtensions", "getBitmap: ${e.message}")
    }
    return bitmap
}
