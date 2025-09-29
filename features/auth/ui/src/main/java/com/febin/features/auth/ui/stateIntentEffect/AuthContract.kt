package com.febin.features.auth.ui.stateIntentEffect

interface ViewState

// A generic interface for MVI intents
interface ViewIntent

// A generic interface for MVI effects
interface ViewEffect

// --- Login ---
data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val email: String = "",
    val password: String = ""
) : ViewState

sealed class LoginIntent : ViewIntent {
    data class EmailChanged(val email: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    object LoginClicked : LoginIntent()
//    data class GoogleSignIn(val idToken: String) : LoginIntent()
}

sealed class LoginEffect : ViewEffect {
    object NavigateToHome : LoginEffect()
    object NavigateToForgotPassword : LoginEffect()
    object NavigateToSignUp : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}


// --- Signup ---
data class SignupState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val fellowship: String = "",
    val role: String = ""

) : ViewState

sealed class SignupIntent : ViewIntent {
    data class FullnameChanged(val fullname: String) : SignupIntent()
    data class EmailChanged(val email: String) : SignupIntent()
    data class PhoneChanged(val phone: String) : SignupIntent()
    data class PasswordChanged(val password: String) : SignupIntent()
    data class FellowshipChanged(val fellowship: String) : SignupIntent()
    data class RoleChanged(val role: String) : SignupIntent()
    object SignupClicked : SignupIntent()
}

sealed class SignupEffect : ViewEffect {
    object NavigateToLogin : SignupEffect()
    data class ShowError(val message: String) : SignupEffect()
}