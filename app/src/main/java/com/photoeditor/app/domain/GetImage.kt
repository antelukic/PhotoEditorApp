package com.photoeditor.app.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface GetImage {

    operator fun invoke(): Flow<Bitmap>
}
