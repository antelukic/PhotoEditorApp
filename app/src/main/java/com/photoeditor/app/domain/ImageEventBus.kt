package com.photoeditor.app.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class ImageEventBus : PublishImage, GetImage {

    private var imagePublisher = MutableStateFlow<Bitmap?>(null)

    override fun invoke(): Flow<Bitmap> = imagePublisher.filterNotNull()

    override suspend fun invoke(image: Bitmap) = imagePublisher.emit(image)
}
