package com.febin.features.userdashboard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febin.core.data.local.AppPreferences
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.features.userdashboard.domain.usecase.GetCurrentUserUseCase
import com.febin.features.userdashboard.domain.usecase.LogoutUseCase
import com.febin.features.userdashboard.ui.contracts.UserProfileEffect
import com.febin.features.userdashboard.ui.contracts.UserProfileEffect.*
import com.febin.features.userdashboard.ui.contracts.UserProfileIntent
import com.febin.features.userdashboard.ui.contracts.UserProfileState

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    private val _effect = Channel<UserProfileEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onIntent(UserProfileIntent.FetchUserProfile)
    }

    fun onIntent(intent: UserProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                UserProfileIntent.FetchUserProfile -> fetchUserProfile()
                UserProfileIntent.Logout -> logout()
            }
        }
    }

    private suspend fun fetchUserProfile() {
        _state.value = _state.value.copy(isLoading = true)
        getCurrentUserUseCase().collectLatest { emission ->
            when (emission) {
                is Result.Failure -> {
                    val failure = emission.failure
                    when (failure) {
                        is Failure.AuthError -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError(failure.message))

                        }
                        Failure.NetworkError -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError("Network error. Please check your internet connection."))
                        }
                        Failure.ServerError -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError("Server error. Please try again later."))
                        }
                        is Failure.Unknown -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError("An unknown error occurred."))
                        }
                    }

                }
                is Result.IsFailure -> TODO()
                Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)

                }
                is Result.Success<*> -> {
                    val user = (emission as Result.Success<com.febin.core.domain.model.User>).data
                    _state.value = _state.value.copy(isLoading = false, user = user)

                }
            }
        }
    }
    private suspend fun logout() {
        logoutUseCase().collectLatest { emission ->
            when (emission) {
                is Result.Failure -> {
                    val failure = emission.failure
                    when (failure) {
                        is Failure.AuthError -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError(failure.message))
                            _effect.send(NavigateToLogin)
                            appPreferences.clearUserInfo()
                        }
                        Failure.NetworkError -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError("Network error. Please check your internet connection."))
                        }
                        Failure.ServerError -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError("Server error. Please try again later."))
                        }
                        is Failure.Unknown -> {
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.send(ShowError("An unknown error occurred."))
                        }
                    }
                }
                is Result.IsFailure -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _effect.send(ShowError("An unknown error occurred."))
                }
                Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                is Result.Success<*> -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _effect.send(NavigateToLogin)
                    appPreferences.clearUserInfo()
                }
            }
        }


    }
}