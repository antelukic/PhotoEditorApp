package com.photoeditor.app

import android.app.Application
import com.photoeditor.app.domain.di.domainModule
import com.photoeditor.app.presentation.di.presentationModule
import org.koin.core.context.startKoin

class PhotoEditorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    presentationModule,
                    domainModule,
                )
            )
        }
    }
}
