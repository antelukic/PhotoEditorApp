package com.photoeditor.app.domain.pickedimage

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class PickedImageEventBus : GetPickedImage, PublishPickedImage {

    private val imagePublisher = MutableStateFlow<Bitmap?>(null)

    override fun pickedImage(): Flow<Bitmap> = imagePublisher
        .asStateFlow()
        .filterNotNull()

    override suspend fun publish(image: Bitmap) {
        imagePublisher.emit(image)
    }
}
