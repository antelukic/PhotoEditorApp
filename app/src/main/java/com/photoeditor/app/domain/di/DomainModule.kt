package com.photoeditor.app.domain.di

import com.photoeditor.app.domain.GetImage
import com.photoeditor.app.domain.ImageEventBus
import com.photoeditor.app.domain.PublishImage
import org.koin.dsl.binds
import org.koin.dsl.module

val domainModule = module {

    single { ImageEventBus() } binds arrayOf(PublishImage::class, GetImage::class)
}
