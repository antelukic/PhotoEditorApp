package com.photoeditor.app.domain.di

import com.photoeditor.app.domain.pickedimage.GetPickedImage
import com.photoeditor.app.domain.pickedimage.PickedImageEventBus
import com.photoeditor.app.domain.pickedimage.PublishPickedImage
import org.koin.dsl.binds
import org.koin.dsl.module

val domainModule = module {

    single { PickedImageEventBus() } binds arrayOf(PublishPickedImage::class, GetPickedImage::class)
}
