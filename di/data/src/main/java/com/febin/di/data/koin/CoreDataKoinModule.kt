package com.febin.di.data.koin

import android.content.Context
import android.content.SharedPreferences
import com.febin.core.data.local.AppPreferences // Your interface
import com.febin.core.data.local.AppPreferencesImpl // Your concrete implementation
import com.febin.core.data.network.createHttpClient
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {

    // 1. Provide SharedPreferences
    single<SharedPreferences> {
        // MODIFIED: Standardized SharedPreferences file name
        androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    // 2. Provide AppPreferencesImpl, injecting SharedPreferences
    single<AppPreferences> { AppPreferencesImpl(prefs = get()) }

    // 3. Ktor HttpClient
    single<HttpClient> { createHttpClient(get()) }

}
