package com.photoeditor.app.domain.di

import com.photoeditor.app.domain.GetImage
import com.photoeditor.app.domain.editedimage.EditedImageEventBus
import com.photoeditor.app.domain.editedimage.GetEditedImage
import com.photoeditor.app.domain.editedimage.PublishEditedImage
import com.photoeditor.app.domain.pickedimage.GetPickedImage
import com.photoeditor.app.domain.pickedimage.PickedImageEventBus
import com.photoeditor.app.domain.pickedimage.PublishPickedImage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val domainModule = module {

    single { PickedImageEventBus() } binds arrayOf(PublishPickedImage::class, GetPickedImage::class)
    single { EditedImageEventBus() } binds arrayOf(PublishEditedImage::class, GetEditedImage::class)

    singleOf(::GetImage)
}
