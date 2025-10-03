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
import com.febin.features.auth.ui.utils.SigninValidator
import com.febin.features.auth.ui.utils.SignupValidator
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
                    is SignupIntent.FullNameChanged -> _state.value = _state.value.copy(fullName = intent.fullName, fullNameError = null)

                    is SignupIntent.EmailChanged -> _state.value = _state.value.copy(email = intent.email, emailError = null)

                    is SignupIntent.PasswordChanged -> _state.value = _state.value.copy(password = intent.password, passwordError = null)

                    is SignupIntent.PhoneChanged -> _state.value = _state.value.copy(phone = intent.phone, phoneError = null)

                    is SignupIntent.FellowshipChanged -> _state.value = _state.value.copy(fellowship = intent.fellowship, fellowshipError = null)

                    is SignupIntent.RoleChanged -> _state.value = _state.value.copy(role = intent.role, roleError = null)

                    is SignupIntent.ClearError -> _state.value = _state.value.copy(error = null)
                    is SignupIntent.SignupClicked -> callSignup()

                }
            }
        }
    }
    private fun callSignup() {
        viewModelScope.launch {
            val currentState = _state.value
            val fullNameError = SignupValidator().validateFullName(currentState.fullName)
            val emailError = SignupValidator().validateEmail(currentState.email)
            val passwordError = SignupValidator().validatePassword(currentState.password)
            val phoneError = SignupValidator().validatePhone(currentState.phone)
            val fellowshipError = SignupValidator().validateFellowship(currentState.fellowship)
            val roleError = SignupValidator().validateRole(currentState.role)

            _state.value = currentState.copy(
                fullNameError = fullNameError,
                emailError = emailError,
                passwordError = passwordError,
                phoneError = phoneError,
                fellowshipError = fellowshipError,
                roleError = roleError,
                isLoading = false
            )


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
                        _effect.send(SignupEffect.NavigateToLogin)
                        _effect.send(SignupEffect.ShowSuccess("Signup successful"))
                    }

                    is DomainResult.Failure -> {
                        val coreFailure: Failure = emission.failure
                        val authError: AuthError = mapCoreFailureToAuthError(coreFailure)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = authError.userMessage()
                        )
                        _effect.send(SignupEffect.ShowError(authError.userMessage()))
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




