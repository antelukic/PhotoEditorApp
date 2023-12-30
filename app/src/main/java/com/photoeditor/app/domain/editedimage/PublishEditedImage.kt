package com.photoeditor.app.domain.editedimage

import android.graphics.Bitmap

interface PublishEditedImage {

    suspend fun publish(image: Bitmap)
}
