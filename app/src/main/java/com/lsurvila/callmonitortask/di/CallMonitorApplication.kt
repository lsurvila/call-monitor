package com.lsurvila.callmonitortask.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CallMonitorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CallMonitorApplication)
            modules(appModule)
        }
    }
}