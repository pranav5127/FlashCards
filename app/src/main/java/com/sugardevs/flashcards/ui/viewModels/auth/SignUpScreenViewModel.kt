package com.sugardevs.flashcards.ui.viewModels.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(): ViewModel() {

    var userName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    fun updateUserName(user: String) {
        userName = user
    }

    fun updatePassword(passwd: String) {
        password = passwd
    }

    fun updateConfirmPassword(confirmPasswd: String) {
        confirmPassword =  confirmPasswd
    }
}