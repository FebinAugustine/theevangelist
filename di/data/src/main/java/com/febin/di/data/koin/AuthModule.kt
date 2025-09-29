package com.febin.di.data.koin

import android.content.Context
import android.content.SharedPreferences
import com.febin.core.data.local.AppPreferences
import com.febin.core.data.local.AppPreferencesImpl
import com.febin.core.data.network.createHttpClient
import com.febin.features.auth.data.remote.services.AuthApiService
import com.febin.features.auth.data.remote.services.AuthApiServiceImpl
import com.febin.features.auth.data.repository.AuthRepositoryImpl
import com.febin.features.auth.domain.repository.AuthRepository
import com.febin.features.auth.domain.useCases.SignupUseCase
import com.febin.features.auth.ui.viewmodel.SignupViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

val authModule = module {

    // Core Data Dependencies
    single<SharedPreferences> {
        androidContext().getSharedPreferences("app_prefs_auth_module", Context.MODE_PRIVATE)
    }
    single<AppPreferences> { AppPreferencesImpl(prefs = get()) }
    single<HttpClient> { createHttpClient(appPreferences = get()) }

    // Auth Data Dependencies
    single<AuthApiService> { AuthApiServiceImpl(httpClient = get()) }
    single<AuthRepository> { AuthRepositoryImpl(authApiService = get(), appPreferences = get()) }

    // Auth Domain Dependencies
    factory { SignupUseCase(authRepository = get()) } // Use factory for UseCases if they are lightweight and stateless

    // Auth UI Dependencies (ViewModels)
    viewModel { SignupViewModel(signupUseCase = get<SignupUseCase>()) } // Explicit type for get()
}
