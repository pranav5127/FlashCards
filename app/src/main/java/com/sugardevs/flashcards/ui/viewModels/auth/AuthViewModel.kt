package com.sugardevs.flashcards.ui.viewModels.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.auth.AuthResponse
import com.sugardevs.flashcards.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var userDisplayName by mutableStateOf("Anonymous")
        private set

    private val _avatarUrl = MutableStateFlow("")
    val avatarUrl: StateFlow<String> = _avatarUrl

    var userName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    var confirmVisible by mutableStateOf(false)
        private set

    var resetEmail by mutableStateOf("")
        private set

    fun updateResetEmail(newEmail: String) { resetEmail = newEmail }
    fun updateUserName(newValue: String) { userName = newValue }
    fun updatePassword(newValue: String) { password = newValue }
    fun updateConfirmPassword(newValue: String) { confirmPassword = newValue }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        confirmVisible = !confirmVisible
    }


    private val _authState = MutableStateFlow<AuthResponse?>(null)
    val authState: StateFlow<AuthResponse?> = _authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn



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

    fun logout(onLogOutComplete: (() -> Unit)? = null) {
        Log.d("AuthViewModel", "Logging out")
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _authState.value = null
            userName = ""
            password = ""
            confirmPassword = ""
            resetEmail = ""
            userDisplayName = "Anonymous"
            _avatarUrl.value = ""
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
            _isLoggedIn.value = session != null

            session?.user?.let { user ->
                userDisplayName = user.userMetadata?.get("full_name")?.toString() ?: "Anonymous"
                val rawAvatar = user.userMetadata?.get("avatar_url")?.toString() ?: ""
                _avatarUrl.value = rawAvatar.removeSurrounding("\"")
                userName = user.email ?: ""
                userDisplayName = userDisplayName.removeSurrounding("\"")
                Log.d("AuthViewModel", " Avatar: $avatarUrl")
            }

            _isLoading.value = false
        }
    }



}
