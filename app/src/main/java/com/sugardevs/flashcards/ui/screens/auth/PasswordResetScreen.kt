package com.sugardevs.flashcards.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun PasswordResetScreen(accessToken: String) {
    val viewModel: AuthViewModel = hiltViewModel()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Reset Your Password", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (newPassword != confirmPassword) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Passwords do not match")
                    }
                    return@Button
                }

                viewModel.setAccessTokenAndReset(accessToken, newPassword)

                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Password reset successful")
                }
            }) {
                Text("Submit")
            }
        }
    }
}
