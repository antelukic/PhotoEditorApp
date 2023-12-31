package com.photoeditor.app.domain.editedimage

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditedImageEventBus : GetEditedImage, PublishEditedImage {

    private val imagePublisher = MutableStateFlow<Bitmap?>(null)

    override fun image(): Flow<Bitmap?> = imagePublisher
        .asStateFlow()

    override suspend fun publish(image: Bitmap) {
        imagePublisher.emit(image)
    }
}

