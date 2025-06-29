package com.sugardevs.flashcards.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.data.auth.AuthResponse
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    val authState by authViewModel.authState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(authState) {
        Log.d("SignUpScreen", "authState changed: $authState")
        when (val state = authState) {
            is AuthResponse.Success -> {
                Log.d("SignUpScreen", "Auth successful. Navigating forward.")
                authViewModel.clearAuthState()
                onSignUpSuccess()
            }
            is AuthResponse.Error -> {
                Log.e("SignUpScreen", "Auth failed: ${state.message}")
                errorMessage = state.message
                authViewModel.clearAuthState()
            }
            else -> Log.d("SignUpScreen", "Auth state is idle or null.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                Log.d("SignUpScreen", "Google Sign-In button clicked")
                authViewModel.signInWithGoogle(context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Sign In With Google")
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(8.dp)
                .height(8.dp)
        )

        OutlinedTextField(
            value = authViewModel.userName,
            onValueChange = {
                Log.d("SignUpScreen", "Username changed: $it")
                authViewModel.updateUserName(it)
            },
            placeholder = { Text(stringResource(R.string.username)) },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = authViewModel.password,
            onValueChange = {
                Log.d("SignUpScreen", "Password changed.")
                authViewModel.updatePassword(it)
            },
            placeholder = { Text(stringResource(R.string.password)) },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = authViewModel.confirmPassword,
            onValueChange = {
                Log.d("SignUpScreen", "Confirm Password changed.")
                authViewModel.updateConfirmPassword(it)
            },
            placeholder = { Text(stringResource(R.string.confirm_password)) },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        errorMessage?.let {
            Log.e("SignUpScreen", "Displaying error: $it")
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("SignUpScreen", "Sign-Up button clicked")
                authViewModel.signUp()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Sign Up")
        }

        TextButton(
            onClick = {
                Log.d("SignUpScreen", "Navigate to Sign-In clicked")
                onNavigateToSignIn()
            },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Already have an account? Sign In")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
