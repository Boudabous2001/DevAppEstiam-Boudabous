package com.example.estiamapp.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val userEmail: String? = null,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "AuthViewModel"
    }

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val currentUser = auth.currentUser
        _uiState.value = AuthUiState(
            isAuthenticated = currentUser != null,
            userEmail = currentUser?.email
        )
        Log.d(TAG, "Auth status checked: ${currentUser != null}")
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                Log.d(TAG, "Login attempt for: $email")

                val result = auth.signInWithEmailAndPassword(email, password).await()

                _uiState.value = AuthUiState(
                    isLoading = false,
                    isAuthenticated = true,
                    userEmail = result.user?.email,
                    error = null
                )
                Log.d(TAG, "Login successful for: $email")
            } catch (e: Exception) {
                Log.e(TAG, "Login failed", e)
                _uiState.value = AuthUiState(
                    isLoading = false,
                    isAuthenticated = false,
                    userEmail = null,
                    error = e.message ?: "Erreur de connexion"
                )
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                Log.d(TAG, "Register attempt for: $email")

                val result = auth.createUserWithEmailAndPassword(email, password).await()

                _uiState.value = AuthUiState(
                    isLoading = false,
                    isAuthenticated = true,
                    userEmail = result.user?.email,
                    error = null
                )
                Log.d(TAG, "Registration successful for: $email")
            } catch (e: Exception) {
                Log.e(TAG, "Registration failed", e)
                _uiState.value = AuthUiState(
                    isLoading = false,
                    isAuthenticated = false,
                    userEmail = null,
                    error = e.message ?: "Erreur d'inscription"
                )
            }
        }
    }

    fun logout() {
        auth.signOut()
        _uiState.value = AuthUiState(
            isLoading = false,
            isAuthenticated = false,
            userEmail = null,
            error = null
        )
        Log.d(TAG, "User logged out")
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}