package com.photoeditor.app

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoeditor.app.domain.GetImage
import com.photoeditor.app.domain.saveimage.SaveImage
import java.util.Calendar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    getImage: GetImage,
    private val saveImage: SaveImage,
): ViewModel() {

    private val image = MutableStateFlow<Bitmap?>(null)
    val saveError = MutableSharedFlow<Boolean>()

    init {
        getImage()
            .onEach(image::emit)
            .launchIn(viewModelScope)
    }

    fun saveImage(context: Context, mimeType: SaveImage.MimeType) {
        viewModelScope.launch {
            image.value?.let {
                val currentDate = Calendar.getInstance()
                saveError.emit(saveImage(context, it, currentDate.time.toString(), mimeType))
            }
        }
    }
}
