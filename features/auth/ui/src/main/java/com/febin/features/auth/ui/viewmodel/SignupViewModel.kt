package com.febin.features.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result as DomainResult
import com.febin.features.auth.ui.utils.AuthError
import com.febin.features.auth.domain.useCases.SignupUseCase
import com.febin.features.auth.ui.stateIntentEffect.SignupEffect
import com.febin.features.auth.ui.stateIntentEffect.SignupIntent
import com.febin.features.auth.ui.stateIntentEffect.SignupState
import com.febin.features.auth.ui.utils.mapCoreFailureToAuthError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val signupUseCase: SignupUseCase): ViewModel() {
    private val _state = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _state.asStateFlow()

    private val _intent = Channel<SignupIntent>(Channel.BUFFERED)
    val intent = _intent.receiveAsFlow()

    private val _effect = Channel<SignupEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collectLatest {intent ->
                when(intent) {
                    is SignupIntent.FullNameChanged -> _state.value = _state.value.copy(fullName = intent.fullName, error = null)

                    is SignupIntent.EmailChanged -> _state.value = _state.value.copy(email = intent.email, error = null)

                    is SignupIntent.PasswordChanged -> _state.value = _state.value.copy(password = intent.password, error = null)

                    is SignupIntent.PhoneChanged -> _state.value = _state.value.copy(phone = intent.phone, error = null)

                    is SignupIntent.FellowshipChanged -> _state.value = _state.value.copy(fellowship = intent.fellowship, error = null)

                    is SignupIntent.RoleChanged -> _state.value = _state.value.copy(role = intent.role, error = null)

                    is SignupIntent.ClearError -> _state.value = _state.value.copy(error = null)
                    is SignupIntent.SignupClicked -> callSignup()

                }
            }
        }
    }
    private fun callSignup() {
        viewModelScope.launch {
            val currentState = _state.value

            // --- Inline simple validation (returns early with unified AuthError) ---
            fun failValidation(msg: String) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = msg // a plain string field your UI already reads (state.error)
                )
            }

            if (currentState.fullName.isBlank()) return@launch failValidation("Full name cannot be empty.")
            if (currentState.email.isBlank()) return@launch failValidation("Please enter a valid email address.")
            if (currentState.password.isBlank() || currentState.password.length < 8) {
                return@launch failValidation("Password must be 8+ characters and include required complexity.")
            }
            if (currentState.phone.isBlank()) return@launch failValidation("Please enter a valid phone number.")
            if (currentState.fellowship.isBlank()) return@launch failValidation("Please select a fellowship.")
            if (currentState.role.isBlank()) return@launch failValidation("Please select a role.")

            // All validations passed — proceed
            _state.value = _state.value.copy(isLoading = true, error = null)

            signupUseCase(
                currentState.fullName.trim(),
                currentState.email.trim(),
                currentState.password,
                currentState.phone.trim(),
                currentState.fellowship,
                currentState.role.trim()
            ).collectLatest { emission ->
                when (emission) {
                    is DomainResult.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    is DomainResult.Success<*> -> {
                        val user = (emission as DomainResult.Success<User>).data
                        _state.value = _state.value.copy(
                            isLoading = false,
                            user = user,
                            error = null
                        )
                        // Optionally navigate / send effect here (or let UI observe user in state)
                    }

                    is DomainResult.Failure -> {
                        val coreFailure: Failure = emission.failure
                        val authError: AuthError = mapCoreFailureToAuthError(coreFailure)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = authError.userMessage()
                        )
                    }

                    else -> {
                        // Fallback: unexpected emission shape
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Unexpected response. Please try again."
                        )
                    }
                }
            }
        }
    }
    fun sendIntent(intent: SignupIntent) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }
}




