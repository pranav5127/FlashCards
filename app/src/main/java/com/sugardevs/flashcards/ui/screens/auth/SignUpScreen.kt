package com.sugardevs.flashcards.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.data.auth.AuthResponse
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Reactive UI state from ViewModel
    val username = authViewModel.userName
    val password = authViewModel.password
    val confirm = authViewModel.confirmPassword
    val passwordVisible by authViewModel::passwordVisible
    val confirmVisible by authViewModel::confirmVisible

    val isFormValid = username.isNotBlank() &&
            password.isNotBlank() &&
            confirm.isNotBlank() &&
            password == confirm

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthResponse.Success -> {
                authViewModel.clearAuthState()
                onSignUpSuccess()
            }
            is AuthResponse.Error -> {
                authViewModel.clearAuthState()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.oops_something_went_wrong))
                }
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { authViewModel.signInWithGoogle(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(stringResource(R.string.sign_in_with_google))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = authViewModel::updateUserName,
                placeholder = { Text(stringResource(R.string.email)) },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = authViewModel::updatePassword,
                placeholder = { Text(stringResource(R.string.password)) },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = authViewModel::togglePasswordVisibility) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            OutlinedTextField(
                value = confirm,
                onValueChange = authViewModel::updateConfirmPassword,
                placeholder = { Text(stringResource(R.string.confirm_password)) },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = authViewModel::toggleConfirmPasswordVisibility) {
                        Icon(
                            imageVector = if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = authViewModel::signUp,
                enabled = isFormValid,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Sign Up")
            }

            TextButton(
                onClick = onNavigateToSignIn,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("Already have an account? Sign In")
            }
        }
    }
}
