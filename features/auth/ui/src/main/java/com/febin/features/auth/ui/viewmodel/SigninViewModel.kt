package com.febin.features.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.NetworkChecker
import com.febin.features.auth.domain.useCases.SigninUseCase
import com.febin.core.domain.utils.Result as DomainResult
import com.febin.features.auth.ui.stateIntentEffect.SigninEffect
import com.febin.features.auth.ui.stateIntentEffect.SigninIntent
import com.febin.features.auth.ui.stateIntentEffect.SigninState
import com.febin.features.auth.ui.utils.AuthError
import com.febin.features.auth.ui.utils.SigninValidator
import com.febin.features.auth.ui.utils.mapCoreFailureToAuthError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber

@Serializable
data class ApiErrorResponseSignin(
    val message: String? = null,
    val error: String? = null,
    val errors: Map<String, String>? = null
)

class SigninViewModel(
    private val signinUseCase: SigninUseCase,
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val _state = MutableStateFlow(SigninState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SigninEffect>()
    val effect = _effect.receiveAsFlow()

    private val json = Json { ignoreUnknownKeys = true }


    fun onIntent(intent: SigninIntent) {
        viewModelScope.launch {
            when (intent) {
                is SigninIntent.EmailChanged -> {
                    _state.value = _state.value.copy(email = intent.email, emailError = null)
                }
                is SigninIntent.PasswordChanged -> {
                    _state.value = _state.value.copy(password = intent.password, passwordError = null)
                }
                SigninIntent.SigninClicked -> {
                    validateInputsAndAttemptSignin()
                }
            }
        }
    }

    private fun validateInputsAndAttemptSignin() {



        val currentState = _state.value
        val emailError = SigninValidator().validateEmail(currentState.email)
        val passwordError = SigninValidator().validatePassword(currentState.password)

        _state.value = currentState.copy(
            emailError = emailError,
            passwordError = passwordError,
            isLoading = false
        )

        val allValid = emailError == null && passwordError == null
        if (allValid) {
            viewModelScope.launch {
                if (checkInternetConnection()) {
                    performSigninApiCall()
                }
            }
        }
    }
    // Returns true if internet available; emits ShowError and clears loading if not.
    private suspend fun checkInternetConnection(): Boolean {
        val hasInternet = networkChecker.hasInternetConnection()
        if (!hasInternet) {
            _state.value = _state.value.copy(isLoading = false)
            _effect.send(SigninEffect.ShowError("No internet connection. Please check your network and try again."))
            return false
        }
        return true
    }


    private suspend fun performSigninApiCall() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        val email = _state.value.email
        val password = _state.value.password

        Timber.tag("SigninViewModel").d("Attempting sign-in for: email=$email")

        signinUseCase(email, password).collectLatest { emission ->
            when (emission) {
                is DomainResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                is DomainResult.Success<*> -> {
                    val user = (emission as DomainResult.Success<User>).data
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = null
                    )
                    if (user.role?.uppercase() == "SUPER_ADMIN") {
                        _effect.send(SigninEffect.NavigateToAdminDashboard(user.id))
                    } else {
                        _effect.send(SigninEffect.NavigateToUserDashboard(user.id))
                    }
                }
                is DomainResult.Failure -> {
                    val coreFailure: Failure = emission.failure
                    val authError: AuthError = mapCoreFailureToAuthError(coreFailure)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = authError.userMessage()
                    )
                    _effect.send(SigninEffect.ShowError(authError.userMessage()))

                }
                else -> {
                    // Fallback: unexpected emission shape
                    val errorMessage = "Unexpected response. Please try again."
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                    _effect.send(SigninEffect.ShowError(errorMessage))

                }

            }


        }
    }


}
