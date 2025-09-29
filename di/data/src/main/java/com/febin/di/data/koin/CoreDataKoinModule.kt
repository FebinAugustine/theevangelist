package com.febin.core.data.di // Or your chosen DI package in core/data

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
        androidContext().getSharedPreferences("app_prefs_koin", Context.MODE_PRIVATE)
        // You can change "app_prefs_koin" to your desired SharedPreferences file name
    }

    // 2. Provide AppPreferencesImpl, injecting SharedPreferences
    // Koin will see that AppPreferencesImpl needs SharedPreferences, and 'get()' will resolve it from above.
    single<AppPreferences> { AppPreferencesImpl(prefs = get()) }

    // 3. Ktor HttpClient: Needs AppPreferences (Koin will inject the AppPreferencesImpl)
    single<HttpClient> { createHttpClient(appPreferences = get()) }
}
