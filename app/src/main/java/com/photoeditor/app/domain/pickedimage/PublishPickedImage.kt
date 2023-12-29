package com.photoeditor.app.domain.pickedimage

import android.graphics.Bitmap

interface PublishPickedImage {

    suspend fun publish(image: Bitmap)
}
