package com.photoeditor.app.domain

import android.graphics.Bitmap
import com.photoeditor.app.domain.editedimage.GetEditedImage
import com.photoeditor.app.domain.pickedimage.GetPickedImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest

class GetImage(
    private val getEditedImage: GetEditedImage,
    private val getPickedImage: GetPickedImage,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun image(): Flow<Bitmap> = combine(
        getEditedImage.image(),
        getPickedImage.pickedImage(),
    ) { editedImage, pickedImage ->
        editedImage ?: pickedImage
    }.mapLatest {
        it
    }
}
