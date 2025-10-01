package com.febin.features.auth.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.core.domain.utils.exceptionOrNull
import com.febin.core.domain.utils.getOrNull
import com.febin.core.domain.utils.isSuccess
import com.febin.features.auth.domain.useCases.SigninUseCase
import com.febin.features.auth.domain.utils.DomainFailureException
import com.febin.features.auth.ui.stateIntentEffect.SigninEffect
import com.febin.features.auth.ui.stateIntentEffect.SigninIntent
import com.febin.features.auth.ui.stateIntentEffect.SigninState
import com.febin.features.auth.ui.utils.SigninValidator
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
    val error: String? = null,
    val errors: Map<String, String>? = null
)

class SigninViewModel(private val signinUseCase: SigninUseCase) : ViewModel() {
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
                performSigninApiCall()
            }
        }
    }

    private suspend fun performSigninApiCall() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        val email = _state.value.email
        val password = _state.value.password

        Timber.tag("SigninViewModel").d("Attempting sign-in for: email=$email")

        try {
            signinUseCase(email = email, password = password).collect { emitted ->


            }
        } catch (t: Throwable) {
        }
    }








}
