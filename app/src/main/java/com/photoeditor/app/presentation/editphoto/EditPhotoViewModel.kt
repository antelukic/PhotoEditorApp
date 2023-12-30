package com.photoeditor.app.presentation.editphoto

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.editedimage.GetEditedImage
import com.photoeditor.app.domain.editedimage.PublishEditedImage
import com.photoeditor.app.domain.pickedimage.GetPickedImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditPhotoViewModel(
    private val editedImagePublisher: PublishEditedImage,
    getEditedImage: GetEditedImage,
    getPickedImage: GetPickedImage,
) : ViewModel() {

    private val _pickedImage = MutableStateFlow<Bitmap?>(null)
    val pickedImage = _pickedImage.asStateFlow()

    init {
        getPickedImage.pickedImage()
            .onEach { bitmap ->
                _pickedImage.emit(bitmap)
            }.launchIn(viewModelScope)
        getEditedImage.image()
            .onEach { bitmap ->
                _pickedImage.emit(bitmap)
            }.launchIn(viewModelScope)

    }

    fun publishEditedImage(image: Bitmap) {
        viewModelScope.launch {
            editedImagePublisher.publish(image)
        }
    }
}
