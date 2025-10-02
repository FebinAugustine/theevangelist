package com.febin.di.data.koin

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Koin initializer.
 * - Call initKoin(context = this) in MainActivity.
 */
fun initKoin(
    androidContext: Context,
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        androidContext(androidContext)
        appDeclaration.invoke(this)
        modules(
            listOf(
                appModule,
                coreDataModule,
                authModule,
                networkModule
            )
        )
    }
}