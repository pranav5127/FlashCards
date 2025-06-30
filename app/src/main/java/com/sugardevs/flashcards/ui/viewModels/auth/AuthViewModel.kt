package com.sugardevs.flashcards.ui.viewModels.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.auth.AuthResponse
import com.sugardevs.flashcards.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Input field state
    var userName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    // Visibility toggles (for password fields)
    var passwordVisible by mutableStateOf(false)
        private set

    var confirmVisible by mutableStateOf(false)
        private set

    fun updateUserName(newValue: String) { userName = newValue }
    fun updatePassword(newValue: String) { password = newValue }
    fun updateConfirmPassword(newValue: String) { confirmPassword = newValue }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        confirmVisible = !confirmVisible
    }

    fun clearInputState() {
        userName = ""
        password = ""
        confirmPassword = ""
        passwordVisible = false
        confirmVisible = false
    }

    private val _authState = MutableStateFlow<AuthResponse?>(null)
    val authState: StateFlow<AuthResponse?> = _authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun signUp() {
        Log.d("AuthViewModel", "Attempting sign up with: $userName")
        if (userName.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Log.e("AuthViewModel", "Fields are empty")
            _authState.value = AuthResponse.Error("Fields cannot be empty")
            return
        }

        if (password != confirmPassword) {
            Log.e("AuthViewModel", "Passwords do not match")
            _authState.value = AuthResponse.Error("Passwords do not match")
            return
        }

        authRepository.signUpWithEmail(userName, password)
            .onEach {
                Log.d("AuthViewModel", "Sign up result: $it")
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun signIn(email: String, password: String) {
        Log.d("AuthViewModel", "Attempting sign in with: $email")
        authRepository.signInWithEmail(email, password)
            .onEach {
                Log.d("AuthViewModel", "Sign in result: $it")
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun signInWithGoogle(context: Context) {
        Log.d("AuthViewModel", "Initiating Google Sign-In")
        authRepository.signInWithGoogle(context)
            .onEach {
                Log.d("AuthViewModel", "Google Sign-In result: $it")
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun sendPasswordResetEmail(email: String) {
        Log.d("AuthViewModel", "Sending password reset to: $email")
        authRepository.sendPasswordResetEmail(email)
            .onEach {
                Log.d("AuthViewModel", "Password reset result: $it")
                _authState.value = it
            }
            .launchIn(viewModelScope)
    }

    fun resetPassword(newPassword: String) {
        Log.d("AuthViewModel", "Attempting password update")
        authRepository.updatePassword(newPassword)
            .onEach {
                Log.d("AuthViewModel", "Password update result: $it")
                _authState.value = it
                if (it is AuthResponse.Success) checkAuthStatus()
            }
            .launchIn(viewModelScope)
    }

    fun logout(onLogOutComplete: (() -> Unit)? = null) {
        Log.d("AuthViewModel", "Logging out")
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _session.value = null
            Log.d("AuthViewModel", "Logout complete")
            onLogOutComplete?.invoke()
        }
    }

    fun clearAuthState() {
        Log.d("AuthViewModel", "Clearing auth state")
        _authState.value = null
    }

    fun checkAuthStatus() {
        Log.d("AuthViewModel", "Checking auth session")
        viewModelScope.launch {
            _isLoading.value = true
            val session = authRepository.getSession()
            _session.value = session
            _isLoggedIn.value = session != null
            _isLoading.value = false
            Log.d("AuthViewModel", "Session: $session")
        }
    }
}
