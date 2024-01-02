package com.photoeditor.app.presentation.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.PublishImage
import kotlinx.coroutines.launch

class HomeViewModel(
    private val publishImage: PublishImage,
) : ViewModel() {

    fun publishPickedImage(image: Bitmap) {
        viewModelScope.launch {
            publishImage(image)
        }
    }
}
