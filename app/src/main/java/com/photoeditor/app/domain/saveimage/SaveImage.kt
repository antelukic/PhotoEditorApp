package com.photoeditor.app.domain.saveimage

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveImage {

    suspend operator fun invoke(context: Context, bitmap: Bitmap, displayName: String, mimeType: MimeType): Boolean =
        withContext(Dispatchers.IO) {
            val contentResolver: ContentResolver = context.contentResolver

            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val imageDetails = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType.type)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }

            val imageUri: Uri? = contentResolver.insert(imageCollection, imageDetails)

            return@withContext imageUri?.let { uri ->
                try {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                    outputStream?.use {
                        when (mimeType) {
                            MimeType.JPEG -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                            MimeType.PNG -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                        }
                    }
                    outputStream?.flush()
                    outputStream?.close()
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            } ?: false
        }

    enum class MimeType(val type: String) {
        JPEG("image/jpeg"), PNG("image/png")
    }
}
