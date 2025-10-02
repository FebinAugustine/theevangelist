package com.febin.features.auth.ui.stateIntentEffect

import com.febin.core.domain.model.User


interface ViewState
// A generic interface for MVI intents
interface ViewIntent

// A generic interface for MVI effects
interface ViewEffect

// --- Login ---
data class SigninState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,
) : ViewState

sealed class SigninIntent : ViewIntent {
    data class EmailChanged(val email: String) : SigninIntent()
    data class PasswordChanged(val password: String) : SigninIntent()
    object SigninClicked : SigninIntent()
//    data class GoogleSignIn(val idToken: String) : LoginIntent()
}

sealed class SigninEffect : ViewEffect { // Base class is ViewEffect
    // MODIFIED: Replaced NavigateToHome with role-based navigation
    data class NavigateToUserDashboard(val userId: String) : SigninEffect()
    data class NavigateToAdminDashboard(val userId: String) : SigninEffect()
    data class ShowError(val message: String) : SigninEffect()
    object NavigateToForgotPassword : SigninEffect()
    object NavigateToSignup : SigninEffect()
}


// --- Signup ---
data class SignupState(
    val isLoading: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val fellowship: String = "",
    val role: String = "",


    // New fields for validation errors
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val phoneError: String? = null,
    val fellowshipError: String? = null,
    val roleError: String? = null,
    val error: String? = null, // General API error

    val user: User? = null,


    ) : ViewState

sealed class SignupIntent : ViewIntent {
    data class FullNameChanged(val fullName: String) : SignupIntent()
    data class EmailChanged(val email: String) : SignupIntent()
    data class PhoneChanged(val phone: String) : SignupIntent()
    data class PasswordChanged(val password: String) : SignupIntent()
    data class FellowshipChanged(val fellowship: String) : SignupIntent()
    data class RoleChanged(val role: String) : SignupIntent()
    data object ClearError : SignupIntent()
    object SignupClicked : SignupIntent()
}

sealed class SignupEffect : ViewEffect {
    object NavigateToLogin : SignupEffect()
    data class ShowError(val message: String) : SignupEffect()
}