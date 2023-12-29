package com.photoeditor.app.presentation.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.pickedimage.PublishPickedImage
import kotlinx.coroutines.launch

class HomeViewModel(
    private val publishPickedImage: PublishPickedImage
) : ViewModel() {

    fun publishPickedImage(image: Bitmap) {
        viewModelScope.launch {
            publishPickedImage.publish(image)
        }
    }
}
