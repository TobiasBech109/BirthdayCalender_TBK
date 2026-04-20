package com.example.birthdaycalender

import android.app.Application
import com.example.birthdaycalender.dependencyinjection.appModules
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModules)
        }
    }
}