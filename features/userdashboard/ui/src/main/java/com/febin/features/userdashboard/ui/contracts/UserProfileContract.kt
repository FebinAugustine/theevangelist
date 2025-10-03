package com.febin.features.userdashboard.ui.contracts

import com.febin.core.domain.model.User

interface ViewState

// A generic interface for MVI intents
interface ViewIntent

// A generic interface for MVI effects
interface ViewEffect

data class UserProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
) : ViewState

sealed class UserProfileIntent : ViewIntent {
    object FetchUserProfile : UserProfileIntent()
    object Logout : UserProfileIntent()
}

sealed class UserProfileEffect : ViewEffect {
    data class ShowError(val message: String) : UserProfileEffect()
    object NavigateToLogin : UserProfileEffect()
}