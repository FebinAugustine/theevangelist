package com.febin.features.auth.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.core.domain.model.User // Ensure User is imported
import com.febin.features.auth.domain.useCases.SigninUseCase
// MODIFIED: Corrected imports for DomainFailureException and Failure
import com.febin.core.domain.utils.DomainFailureException 
import com.febin.core.domain.utils.Failure 
import com.febin.features.auth.ui.stateIntentEffect.SigninEffect
import com.febin.features.auth.ui.stateIntentEffect.SigninIntent
import com.febin.features.auth.ui.stateIntentEffect.SigninState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber


@Serializable
data class ApiErrorResponseSignin(
    val message: String? = null,
    val error: String? = null, // For simpler error messages from some APIs
    val errors: Map<String, String>? = null // For detailed field errors (e.g., Spring Boot validation)
)

class SigninViewModel(private val signinUseCase: SigninUseCase) : ViewModel() {
    private val _state = MutableStateFlow(SigninState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SigninEffect>()
    val effect = _effect.receiveAsFlow()

    private val json = Json { ignoreUnknownKeys = true } // JSON parser instance

    // --- Validation Helper Functions ---

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email is required."
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email format."
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password is required."
        // Consider aligning with backend password policies if they are stricter or different
        if (password.length < 8) return "Password must be at least 8 characters."
        if (password.length > 100) return "Password cannot exceed 100 characters."
        return null
    }

    fun onIntent(intent: SigninIntent) {
        viewModelScope.launch {
            when (intent) {
                is SigninIntent.EmailChanged -> {
                    _state.value = _state.value.copy(
                        email = intent.email,
                        emailError = null
                    )
                }
                is SigninIntent.PasswordChanged -> {
                    _state.value = _state.value.copy(
                        password = intent.password,
                        passwordError = null
                    )
                }
                SigninIntent.SigninClicked -> {
                    validateInputsAndAttemptSignin()
                }
            }
        }
    }

    private fun validateInputsAndAttemptSignin() {
        val currentState = _state.value
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)

        _state.value = currentState.copy(
            emailError = emailError,
            passwordError = passwordError,
            isLoading = false // Reset loading state if validation fails before API call
        )
        val allValidationsPassed = emailError == null && passwordError == null

        if (allValidationsPassed) {
            viewModelScope.launch {
                performSigninApiCall()
            }
        }
    }

    private suspend fun performSigninApiCall() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val email = _state.value.email
        val password = _state.value.password

        Timber.tag("SigninViewModel").d("Attempting sign-in for: email=$email")
        signinUseCase(
            email = email,
            password = password
        ).onSuccess { loggedInUser: User -> 
            Timber.tag("SigninViewModel").d("Signin success. User ID: ${loggedInUser.id}, Role: ${loggedInUser.role}")
            _state.value = _state.value.copy(isLoading = false)
            
            if (loggedInUser.role?.equals("ADMIN", ignoreCase = true) == true || loggedInUser.role?.equals("SUPER_ADMIN", ignoreCase = true) == true) { // MODIFIED to include SUPER_ADMIN
                _effect.send(SigninEffect.NavigateToAdminDashboard(loggedInUser.id))
            } else {
                _effect.send(SigninEffect.NavigateToUserDashboard(loggedInUser.id))
            }

        }.onFailure { throwable -> 
            val actualFailure = (throwable as? DomainFailureException)?.domainFailure

            val userFriendlyMessage = if (actualFailure != null) {
                when (actualFailure) {
                    is Failure.NetworkError -> actualFailure.message 
                    is Failure.HttpError -> {
                        val parsedError = actualFailure.errorBody?.let {
                            try {
                                json.decodeFromString<ApiErrorResponseSignin>(it)
                            } catch (e: Exception) {
                                Timber.e(e, "Failed to parse API error body: ${actualFailure.errorBody}")
                                null
                            }
                        }
                        when (actualFailure.code) {
                            400 -> parsedError?.message ?: "Invalid credentials or bad request."
                            401 -> parsedError?.message ?: "Incorrect email or password."
                            403 -> parsedError?.message ?: "Access forbidden."
                            404 -> "Sign-in endpoint not found. Please contact support."
                            in 400..499 -> "Client error: ${parsedError?.message ?: actualFailure.message} (Code: ${actualFailure.code})"
                            in 500..599 -> "Server error (Code: ${actualFailure.code}). Please try again later."
                            else -> actualFailure.message
                        }
                    }
                    is Failure.GenericError -> actualFailure.message
                    is Failure.AuthError -> "Authentication error. Please try signing in again." // MODIFIED: Handled AuthError
                }
            } else {
                Timber.e(throwable, "SigninViewModel: Error in onFailure, but actualFailure is null or not DomainFailureException. Exception: ${throwable.message}")
                "An unexpected error occurred. Please try again."
            }
            Timber.tag("SigninViewModel")
                .d("Signin failure: $userFriendlyMessage. Original DomainFailure: $actualFailure, Wrapped Exception: $throwable")
            _state.value = _state.value.copy(isLoading = false, error = userFriendlyMessage)
            _effect.send(SigninEffect.ShowError(userFriendlyMessage))
        }
    }
}
