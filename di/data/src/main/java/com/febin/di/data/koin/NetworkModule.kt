package com.febin.di.data.koin

import com.febin.core.data.utils.NetworkCheckerImpl
import com.febin.core.domain.utils.NetworkChecker
import org.koin.dsl.module

val networkModule = module {
    // Internet checker
    single<NetworkChecker> { NetworkCheckerImpl(get()) }
}