package com.photoeditor.app.presentation.paintimage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.GetImage
import com.photoeditor.app.domain.PublishImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PaintViewModel(
    getImage: GetImage,
    private val publishImage: PublishImage
) : ViewModel() {

    private val _originalImage = MutableStateFlow<Bitmap?>(null)
    val originalImage = _originalImage.asStateFlow()

    init {
        getImage()
            .onEach(_originalImage::emit)
            .launchIn(viewModelScope)
    }

    fun publishPaintedImage(image: Bitmap) {
        viewModelScope.launch {
            publishImage(image)
        }
    }
}
