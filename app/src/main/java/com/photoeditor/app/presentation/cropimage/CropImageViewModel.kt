package com.photoeditor.app.presentation.cropimage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.GetImage
import com.photoeditor.app.domain.PublishImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CropImageViewModel(
    getImage: GetImage,
    private val publishImage: PublishImage
) : ViewModel() {

    private var _image = MutableStateFlow<Bitmap?>(null)
    val image = _image.asStateFlow().filterNotNull()

    init {
        getImage()
            .onEach {
                _image.emit(it)
            }.launchIn(viewModelScope)
    }

    fun publishCroppedImage(image: Bitmap) {
        viewModelScope.launch {
            publishImage(image)
        }
    }
}
