package com.febin.di.data.koin

import com.febin.di.data.SharedPreferencesManager
import org.koin.dsl.module

/**
 * App-specific Koin module for overrides or app-level singles.
 * - e.g., App context, shared prefs.
 * - Extend as needed.
 */
val appModule = module {
    single { SharedPreferencesManager(get()) }  // Bind manager with Context
}