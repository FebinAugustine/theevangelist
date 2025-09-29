package com.febin.features.auth.ui.viewmodel

import android.util.Log
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

    fun onIntent(intent: SignupIntent) {
        viewModelScope.launch {
            when (intent) {
                is SignupIntent.FullnameChanged -> {
                    _state.value = _state.value.copy(fullname = intent.fullname)
                }
                is SignupIntent.EmailChanged -> {
                    _state.value = _state.value.copy(email = intent.email)
                }
                is SignupIntent.PhoneChanged -> {
                    _state.value = _state.value.copy(phone = intent.phone)
                }
                is SignupIntent.PasswordChanged -> {
                    _state.value = _state.value.copy(password = intent.password)
                }
                is SignupIntent.FellowshipChanged -> {
                    _state.value = _state.value.copy(fellowship = intent.fellowship)
                }
                is SignupIntent.RoleChanged -> {
                    _state.value = _state.value.copy(role = intent.role)
                }
                SignupIntent.SignupClicked -> {
                    signup()
                }
            }
        }
    }

    private suspend fun signup() {
        _state.value = _state.value.copy(isLoading = true)
        val fullname = _state.value.fullname
        val email = _state.value.email
        val password = _state.value.password
        val phone = _state.value.phone
        Log.d("SignupViewModel", "Register request: fullname=$fullname, email=$email, phone=$phone")
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
                val errorMessage = (throwable as? Failure)?.message ?: "An unknown error occurred"
                Log.d("SignupViewModel", "Register failure: $errorMessage")
                _state.value = _state.value.copy(isLoading = false)
                _effect.send(SignupEffect.ShowError(errorMessage))
            }
    }
}

