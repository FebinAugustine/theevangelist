package com.febin.di.data.koin

import android.content.Context
import android.content.SharedPreferences
import com.febin.core.data.local.AppPreferences
import com.febin.core.data.local.AppPreferencesImpl
import com.febin.core.data.network.createHttpClient
import com.febin.core.data.utils.NetworkCheckerImpl
import com.febin.core.domain.utils.NetworkChecker
import com.febin.features.auth.data.remote.services.AuthApiService
import com.febin.features.auth.data.remote.services.AuthApiServiceImpl
import com.febin.features.auth.data.repository.AuthRepositoryImpl
import com.febin.features.auth.domain.repository.AuthRepository
import com.febin.features.auth.domain.useCases.SigninUseCase
import com.febin.features.auth.domain.useCases.SignupUseCase
import com.febin.features.auth.ui.viewmodel.SigninViewModel
import com.febin.features.auth.ui.viewmodel.SignupViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {
    // shared preference
    single<SharedPreferences> {
        androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    single<AppPreferences> { AppPreferencesImpl(prefs = get()) }

    // Repository
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    // UseCases
    singleOf(::SigninUseCase)
    singleOf(::SignupUseCase)

    // view model
    viewModel { SigninViewModel(get(), get()) }
    viewModel { SignupViewModel(get()) }

    // Api service
    singleOf(::AuthApiServiceImpl) { bind<AuthApiService>() }





}
