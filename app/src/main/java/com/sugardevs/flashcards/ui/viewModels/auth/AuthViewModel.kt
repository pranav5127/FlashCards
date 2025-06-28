package com.sugardevs.flashcards.ui.viewModels.auth

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.auth.AuthResponse
import com.sugardevs.flashcards.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var userName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    fun updateUserName(newValue: String) { userName = newValue }
    fun updatePassword(newValue: String) { password = newValue }
    fun updateConfirmPassword(newValue: String) { confirmPassword = newValue }

    private val _authState = MutableStateFlow<AuthResponse?>(null)
    val authState: StateFlow<AuthResponse?> = _authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun signUp() {
        if (userName.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _authState.value = AuthResponse.Error("Fields cannot be empty")
            return
        }

        if (password != confirmPassword) {
            _authState.value = AuthResponse.Error("Passwords do not match")
            return
        }

        authRepository.signUpWithEmail(userName, password)
            .onEach {
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun signIn(email: String, password: String) {
        authRepository.signInWithEmail(email, password)
            .onEach {
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun signInWithGoogle(context: Context) {
        authRepository.signInWithGoogle(context)
            .onEach {
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun sendPasswordResetEmail(email: String) {
        authRepository.sendPasswordResetEmail(email)
            .onEach { _authState.value = it }
            .launchIn(viewModelScope)
    }

    fun resetPassword(newPassword: String) {
        authRepository.updatePassword(newPassword)
            .onEach {
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun logout(onLogOutComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _session.value = null
            onLogOutComplete?.invoke()
        }
    }

    fun clearAuthState() {
        _authState.value = null
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            val session = authRepository.getSession()
            _session.value = session
            _isLoggedIn.value = session != null
            _isLoading.value = false
        }
    }
}
