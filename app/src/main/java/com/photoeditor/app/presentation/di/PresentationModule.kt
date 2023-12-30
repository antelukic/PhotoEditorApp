package com.photoeditor.app.presentation.di

import com.photoeditor.app.presentation.editphoto.EditPhotoViewModel
import com.photoeditor.app.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { HomeViewModel(get()) }
    viewModel { EditPhotoViewModel(get(), get(), get()) }
}
