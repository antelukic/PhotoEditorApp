package com.photoeditor.app.presentation.addfilter

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.sketchimage.SketchImage
import com.photoeditor.app.domain.GetImage
import com.photoeditor.app.domain.PublishImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddFilterViewModel(
    getImage: GetImage,
    private val publishImage: PublishImage
) : ViewModel() {

    private val _originalImage = MutableStateFlow<Bitmap?>(null)
    val originalImage = this._originalImage.asStateFlow()

    val filteredImage = MutableStateFlow<Bitmap?>(null)
    val tabProgress = mutableMapOf<Int, Int>()

    var selectedTab: Int = SketchImage.ORIGINAL_TO_GRAY

    init {
        getImage()
            .onEach { bitmap ->
                this._originalImage.emit(bitmap)
            }.launchIn(viewModelScope)
    }


    fun publishFilteredImage(image: Bitmap) {
        viewModelScope.launch {
            publishImage(image)
        }
    }
}
