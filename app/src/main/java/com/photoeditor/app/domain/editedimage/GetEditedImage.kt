package com.photoeditor.app.domain.editedimage

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface GetEditedImage {

    fun image(): Flow<Bitmap?>
}
