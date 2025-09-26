package com.febin.theevangelist

import android.app.Application
import com.febin.di.data.koin.initKoin
import timber.log.Timber

class TheEvangelistApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Init Timber for logging
        Timber.plant(Timber.DebugTree())

        // Init Koin for DI
        initKoin(this)
    }
}