package com.febin.features.auth.ui.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.features.auth.domain.useCases.SignupUseCase
import com.febin.features.auth.domain.utils.DomainFailureException // ADDED
import com.febin.features.auth.domain.utils.Failure
import com.febin.features.auth.ui.stateIntentEffect.SignupEffect
import com.febin.features.auth.ui.stateIntentEffect.SignupIntent
import com.febin.features.auth.ui.stateIntentEffect.SignupState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber

// Helper data class for parsing API error responses
@Serializable
data class ApiErrorResponse(
    val message: String? = null,
    val error: String? = null, // For simpler error messages from some APIs
    val errors: Map<String, String>? = null // For detailed field errors (e.g., Spring Boot validation)
)

class SignupViewModel(private val signupUseCase: SignupUseCase) : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SignupEffect>()
    val effect = _effect.receiveAsFlow()

    private val json = Json { ignoreUnknownKeys = true } // JSON parser instance

    // --- Validation Helper Functions ---
    private fun validateFullname(fullname: String): String? {
        if (fullname.isBlank()) return "Full name is required."
        if (fullname.length < 2) return "Full name must be at least 2 characters."
        if (fullname.length > 50) return "Full name cannot exceed 50 characters."
        return null
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email is required."
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email format."
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password is required."
        if (password.length < 8) return "Password must be at least 8 characters."
        if (password.length > 100) return "Password cannot exceed 100 characters."
        if (!password.any { it.isDigit() }) return "Password must contain at least one digit."
        if (!password.any { it.isUpperCase() }) return "Password must contain at least one uppercase letter."
        if (!password.any { it.isLowerCase() }) return "Password must contain at least one lowercase letter."
        if (password.none { !it.isLetterOrDigit() }) return "Password must contain at least one special character."
        return null
    }

    private fun validatePhone(phone: String): String? {
        if (phone.isBlank()) return "Phone number is required."
        if (!phone.all { it.isDigit() }) return "Phone number must contain only digits."
        if (phone.length < 10 || phone.length > 15) return "Phone number must be between 10 and 15 digits."
        return null
    }

    private fun validateSelection(selection: String, fieldName: String): String? {
        if (selection.isBlank()) return "$fieldName is required."
        return null
    }

    fun onIntent(intent: SignupIntent) {
        viewModelScope.launch {
            when (intent) {
                is SignupIntent.FullnameChanged -> {
                    _state.value = _state.value.copy(
                        fullname = intent.fullname,
                        fullnameError = null
                    )
                }
                is SignupIntent.EmailChanged -> {
                    _state.value = _state.value.copy(
                        email = intent.email,
                        emailError = null
                    )
                }
                is SignupIntent.PhoneChanged -> {
                    _state.value = _state.value.copy(
                        phone = intent.phone,
                        phoneError = null
                    )
                }
                is SignupIntent.PasswordChanged -> {
                    _state.value = _state.value.copy(
                        password = intent.password,
                        passwordError = null
                    )
                }
                is SignupIntent.FellowshipChanged -> {
                    _state.value = _state.value.copy(
                        fellowship = intent.fellowship,
                        fellowshipError = null
                    )
                }
                is SignupIntent.RoleChanged -> {
                    _state.value = _state.value.copy(
                        role = intent.role,
                        roleError = null
                    )
                }
                SignupIntent.SignupClicked -> {
                    validateInputsAndAttemptSignup()
                }
            }
        }
    }

    private fun validateInputsAndAttemptSignup() {
        val currentState = _state.value
        val fullnameError = validateFullname(currentState.fullname)
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)
        val phoneError = validatePhone(currentState.phone)
        val fellowshipError = validateSelection(currentState.fellowship, "Fellowship")
        val roleError = validateSelection(currentState.role, "Role")

        _state.value = currentState.copy(
            fullnameError = fullnameError,
            emailError = emailError,
            passwordError = passwordError,
            phoneError = phoneError,
            fellowshipError = fellowshipError,
            roleError = roleError,
            isLoading = false
        )

        val allValidationsPassed = fullnameError == null && emailError == null &&
                passwordError == null && phoneError == null &&
                fellowshipError == null && roleError == null

        if (allValidationsPassed) {
            viewModelScope.launch {
                performSignupApiCall()
            }
        }
    }

    private suspend fun performSignupApiCall() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val fullname = _state.value.fullname
        val email = _state.value.email
        val password = _state.value.password
        val phone = _state.value.phone

        Timber.tag("SignupViewModel")
            .d("Validation passed. Register request: fullname=$fullname, email=$email, phone=$phone")
        signupUseCase(
            fullname = fullname,
            email = email,
            password = password,
            phone = phone,
            fellowship = _state.value.fellowship,
            role = _state.value.role
        )
            .onSuccess {
                Timber.tag("SignupViewModel").d("Register success")
                _state.value = _state.value.copy(isLoading = false)
                _effect.send(SignupEffect.NavigateToLogin)
            }
            .onFailure { throwable -> // throwable is now expected to be DomainFailureException
                val actualFailure = (throwable as? DomainFailureException)?.domainFailure

                val userFriendlyMessage = if (actualFailure != null) {
                    when (actualFailure) {
                        is Failure.NetworkError -> actualFailure.message // Already user-friendly
                        is Failure.HttpError -> {
                            val parsedError = actualFailure.errorBody?.let {
                                try {
                                    json.decodeFromString<ApiErrorResponse>(it)
                                } catch (e: Exception) {
                                    Timber.e(e, "Failed to parse API error body: ${actualFailure.errorBody}")
                                    null
                                }
                            }
                            when (actualFailure.code) {
                                400 -> parsedError?.message ?: "Invalid data sent. Please check your input."
                                401 -> "Unauthorized. Please try logging in again."
                                403 -> "Access forbidden. You do not have permission."
                                409 -> {
                                    val specificConflictMessage = parsedError?.message ?: parsedError?.error
                                    if (specificConflictMessage?.contains("Email already exists", ignoreCase = true) == true) {
                                        "This email address is already registered."
                                    } else if (specificConflictMessage?.contains("Phone number already registered", ignoreCase = true) == true) {
                                        "This phone number is already registered."
                                    } else {
                                        specificConflictMessage ?: "Conflict: The data you entered already exists or conflicts with existing records."
                                    }
                                }
                                in 400..499 -> "Request error: ${parsedError?.message ?: actualFailure.message} (Code: ${actualFailure.code})"
                                in 500..599 -> "Server error (Code: ${actualFailure.code}). Please try again later."
                                else -> actualFailure.message // Default HTTP error message from Failure class
                            }
                        }
                        is Failure.GenericError -> actualFailure.message // Already user-friendly
                    }
                } else {
                    Timber.e(throwable, "Unexpected error type in onFailure: ${throwable.javaClass.name}")
                    "An unexpected error occurred. Please try again."
                }

                Timber.tag("SignupViewModel")
                    .d("Register failure: $userFriendlyMessage. Original DomainFailure: $actualFailure, Wrapped Exception: $throwable")
                _state.value = _state.value.copy(isLoading = false, error = userFriendlyMessage)
                _effect.send(SignupEffect.ShowError(userFriendlyMessage))
            }
    }
}
