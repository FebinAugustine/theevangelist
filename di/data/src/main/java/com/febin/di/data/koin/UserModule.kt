package com.febin.di.data.koin

import com.febin.features.userdashboard.data.remote.UserApiService
import com.febin.features.userdashboard.data.remote.UserApiServiceImpl
import com.febin.features.userdashboard.data.repository.UserRepositoryImpl
import com.febin.features.userdashboard.domain.repository.UserRepository
import com.febin.features.userdashboard.domain.usecase.GetCurrentUserUseCase
import com.febin.features.userdashboard.domain.usecase.LogoutUseCase
import com.febin.features.userdashboard.ui.viewmodel.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userModule = module {
    // User Dashboard View model
    viewModel { UserProfileViewModel(get(), get(), get()) }

    // Use Cases
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::LogoutUseCase)

    // repository
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }

    // api service
    singleOf(::UserApiServiceImpl) { bind<UserApiService>() }

}
