//package com.febin.features.userdashboard.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.febin.core.data.local.AppPreferences
//import com.febin.core.domain.utils.Failure
//import com.febin.features.userdashboard.domain.usecase.GetUserProfileUseCase
//import com.febin.features.userdashboard.domain.usecase.LogoutUseCase
//import com.febin.features.userdashboard.ui.contracts.UserProfileEffect
//import com.febin.features.userdashboard.ui.contracts.UserProfileIntent
//import com.febin.features.userdashboard.ui.contracts.UserProfileState
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.receiveAsFlow
//import kotlinx.coroutines.launch
//
//class UserProfileViewModel(
//    private val getUserProfileUseCase: GetUserProfileUseCase,
//    private val logoutUseCase: LogoutUseCase,
//    private val appPreferences: AppPreferences
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(UserProfileState())
//    val state = _state.asStateFlow()
//
//    private val _effect = Channel<UserProfileEffect>()
//    val effect = _effect.receiveAsFlow()
//
//    init {
//        onIntent(UserProfileIntent.FetchUserProfile)
//    }
//
//    fun onIntent(intent: UserProfileIntent) {
//        viewModelScope.launch {
//            when (intent) {
//                UserProfileIntent.FetchUserProfile -> fetchUserProfile()
//                UserProfileIntent.Logout -> logout()
//            }
//        }
//    }
//
//    private suspend fun fetchUserProfile() {
//        _state.value = _state.value.copy(isLoading = true)
//        getUserProfileUseCase()
//            .onSuccess {
//                _state.value = _state.value.copy(isLoading = false, user = it)
//            }
//            .onFailure { throwable ->
//                _state.value = _state.value.copy(isLoading = false)
//                handleAuthFailure(throwable)
//            }
//    }
//
//    private suspend fun logout() {
//        logoutUseCase().fold(
//            onSuccess = {
//                // After successful backend logout, clear local data and navigate
//                appPreferences.clearUserInfo()
//                _effect.send(UserProfileEffect.NavigateToLogin)
//            },
//            onFailure = { throwable ->
//                // As a fallback, even if the backend call fails, log the user out locally
//                appPreferences.clearUserInfo()
//                _effect.send(UserProfileEffect.NavigateToLogin)
//                handleAuthFailure(throwable) // This will decide whether to show a toast
//            }
//        )
//    }
//
//    private suspend fun handleAuthFailure(throwable: Throwable) {
//        val errorMessage = null ?: throwable.message ?: "An unknown error occurred"
//        if (errorMessage.contains("jwt expired", ignoreCase = true) ||
//            errorMessage.contains("Unauthorized", ignoreCase = true) ||
//            errorMessage.contains("token", ignoreCase = true)) {
//            _effect.send(UserProfileEffect.NavigateToLogin)
//        } else {
//            _effect.send(UserProfileEffect.ShowError(errorMessage))
//        }
//    }
//}