package com.photoeditor.app.presentation.cropimage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.GetImageForCrop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CropImageViewModel(
    getImageForCrop: GetImageForCrop
) : ViewModel() {

    private var _image = MutableStateFlow<Bitmap?>(null)
    val image = _image.asStateFlow().filterNotNull()

    init {
        getImageForCrop.image()
            .onEach {
                _image.emit(it)
            }.launchIn(viewModelScope)
    }

    fun publishCroppedImage(image: Bitmap) {
        image
    }
}