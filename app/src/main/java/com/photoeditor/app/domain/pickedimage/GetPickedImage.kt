package com.photoeditor.app.domain.pickedimage

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface GetPickedImage {

    fun pickedImage(): Flow<Bitmap>
}
