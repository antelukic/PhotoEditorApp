package com.photoeditor.app.presentation.di

import com.photoeditor.app.MainViewModel
import com.photoeditor.app.presentation.addfilter.AddFilterViewModel
import com.photoeditor.app.presentation.cropimage.CropImageViewModel
import com.photoeditor.app.presentation.home.HomeViewModel
import com.photoeditor.app.presentation.paintimage.PaintViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { HomeViewModel(get()) }
    viewModel { AddFilterViewModel(get(), get()) }
    viewModel { CropImageViewModel(get(), get()) }
    viewModel { PaintViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}
