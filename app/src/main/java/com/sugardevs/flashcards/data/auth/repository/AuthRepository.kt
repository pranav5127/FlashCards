package com.sugardevs.flashcards.data.auth.repository

import android.content.Context
import android.util.Log
import com.sugardevs.flashcards.data.auth.AuthResponse
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import java.util.UUID
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.sugardevs.flashcards.BuildConfig
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession

private const val TAG = "AuthRepository"

class AuthRepository {

    private val supabase = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        install(Auth) {
            enableLifecycleCallbacks = true
        }
    }

    fun signUpWithEmail(email: String, password: String): Flow<AuthResponse> = flow {
        try {
            Log.d(TAG, "Signing up with email: $email")
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e(TAG, "Sign up failed: ${e.message}", e)
            emit(AuthResponse.Error(e.message))
        }
    }

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> = flow {
        try {
            Log.d(TAG, "Signing in with email: $email")
            supabase.auth.signInWith(Email) {

                this.email = email
                this.password = password

            }
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e(TAG, "Sign in failed: ${e.message}", e)
            emit(AuthResponse.Error(e.message))
        }
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun signInWithGoogle(context: Context): Flow<AuthResponse> = flow {
        val hashedNonce = createNonce()
        Log.d(TAG, "Starting Google sign-in with nonce: $hashedNonce")

        val googleIdOption = GetGoogleIdOption.Builder()
            .setAutoSelectEnabled(false)
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.SUPABASE_WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        val isAvailable = GoogleApiAvailability
            .getInstance()
            .isGooglePlayServicesAvailable(context)
        Log.d(TAG, "Play Services status: $isAvailable")

        try {
            Log.d(TAG, "Launching credential request...")
            val result = credentialManager.getCredential(context, request)
            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = credential.idToken

            Log.d(TAG, "Google ID Token received: ${idToken.take(10)}...")

            supabase.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }

            Log.d(TAG, "Signed in with Supabase using ID token.")
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e(TAG, "Google Sign-In failed: ${e.localizedMessage}", e)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    suspend fun getSession(): UserSession? {
        Log.d(TAG, "Loading session from storage...")
        supabase.auth.loadFromStorage(autoRefresh = true)
        supabase.auth.awaitInitialization()
        val session = supabase.auth.currentSessionOrNull()
        Log.d(TAG, "Current session: $session")
        return session
    }

    suspend fun logout() {
        Log.d(TAG, "Signing out...")
        supabase.auth.signOut()
        Log.d(TAG, "Signed out.")
    }

    fun sendPasswordResetEmail(email: String): Flow<AuthResponse> = flow {
        try {
            Log.d(TAG, "Sending password reset email to: $email")
            supabase.auth.resetPasswordForEmail(
                email,
                redirectUrl = "https://flash-passwd.vercel.app/"

            )

            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e(TAG, "Password reset failed: ${e.localizedMessage}", e)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    fun updatePassword(newPassword: String): Flow<AuthResponse> = flow {
        try {
            Log.d(TAG, "Updating password.")
            supabase.auth.updateUser {
                password = newPassword
            }
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e(TAG, "Password update failed: ${e.localizedMessage}", e)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }
}
