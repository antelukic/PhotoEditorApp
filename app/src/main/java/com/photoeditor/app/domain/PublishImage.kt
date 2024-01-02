package com.photoeditor.app.domain

import android.graphics.Bitmap

interface PublishImage {

    suspend operator fun invoke(image: Bitmap)
}
