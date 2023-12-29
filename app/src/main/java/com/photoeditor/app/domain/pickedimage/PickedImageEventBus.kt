package com.photoeditor.app.domain.pickedimage

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class PickedImageEventBus : GetPickedImage, PublishPickedImage {

    private var _imagePublisher = MutableStateFlow<Bitmap?>(null)
    private val imagePublisher = _imagePublisher.asStateFlow()

    override fun pickedImage(): Flow<Bitmap> = imagePublisher
        .filterNotNull()

    override suspend fun publish(image: Bitmap) {
        _imagePublisher.emit(image)
    }
}
