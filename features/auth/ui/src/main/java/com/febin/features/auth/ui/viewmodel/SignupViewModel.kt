package com.febin.features.auth.ui.viewmodel

import android.util.Log
import android.util.Patterns // ADDED: For email validation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.features.auth.domain.useCases.SignupUseCase
import com.febin.features.auth.domain.utils.Failure
import com.febin.features.auth.ui.stateIntentEffect.SignupEffect
import com.febin.features.auth.ui.stateIntentEffect.SignupIntent
import com.febin.features.auth.ui.stateIntentEffect.SignupState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val signupUseCase: SignupUseCase) : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SignupEffect>()
    val effect = _effect.receiveAsFlow()

    // --- Validation Helper Functions ---
    private fun validateFullname(fullname: String): String? {
        if (fullname.isBlank()) return "Full name is required."
        if (fullname.length < 2) return "Full name must be at least 2 characters."
        if (fullname.length > 50) return "Full name cannot exceed 50 characters."
        // Add more specific character validation if needed (e.g., regex for allowed chars)
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
                        fullnameError = null // Clear error on change
                    )
                }
                is SignupIntent.EmailChanged -> {
                    _state.value = _state.value.copy(
                        email = intent.email,
                        emailError = null // Clear error on change
                    )
                }
                is SignupIntent.PhoneChanged -> {
                    _state.value = _state.value.copy(
                        phone = intent.phone,
                        phoneError = null // Clear error on change
                    )
                }
                is SignupIntent.PasswordChanged -> {
                    _state.value = _state.value.copy(
                        password = intent.password,
                        passwordError = null // Clear error on change
                    )
                }
                is SignupIntent.FellowshipChanged -> {
                    _state.value = _state.value.copy(
                        fellowship = intent.fellowship,
                        fellowshipError = null // Clear error on change
                    )
                }
                is SignupIntent.RoleChanged -> {
                    _state.value = _state.value.copy(
                        role = intent.role,
                        roleError = null // Clear error on change
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
            isLoading = false // Ensure loading is off if validation fails early
        )

        val allValidationsPassed = fullnameError == null && emailError == null &&
                passwordError == null && phoneError == null &&
                fellowshipError == null && roleError == null

        if (allValidationsPassed) {
            viewModelScope.launch { // Launch coroutine for suspend function call
                performSignupApiCall()
            }
        }
    }

    private suspend fun performSignupApiCall() { // RENAMED from signup()
        _state.value = _state.value.copy(isLoading = true, error = null) // Clear previous API error
        val fullname = _state.value.fullname
        val email = _state.value.email
        val password = _state.value.password
        val phone = _state.value.phone

        Log.d("SignupViewModel", "Validation passed. Register request: fullname=$fullname, email=$email, phone=$phone")
        signupUseCase(
            fullname = fullname,
            email = email,
            password = password,
            phone = phone,
            fellowship = _state.value.fellowship,
            role = _state.value.role
        )
            .onSuccess {
                Log.d("SignupViewModel", "Register success")
                _state.value = _state.value.copy(isLoading = false)
                _effect.send(SignupEffect.NavigateToLogin)
            }
            .onFailure { throwable ->
                val errorMessage = (throwable as? Failure)?.message ?: throwable.message ?: "An unknown error occurred"
                Log.d("SignupViewModel", "Register failure: $errorMessage")
                _state.value = _state.value.copy(isLoading = false, error = errorMessage)
                _effect.send(SignupEffect.ShowError(errorMessage))
            }
    }
}
