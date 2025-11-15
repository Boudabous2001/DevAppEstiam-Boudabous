package com.example.estiamapp.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RequireAuth(
    authViewModel: AuthViewModel,
    onNotAuthenticated: () -> Unit,
    content: @Composable () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isAuthenticated) {
        if (!uiState.isAuthenticated) {
            onNotAuthenticated()
        }
    }

    if (uiState.isAuthenticated) {
        content()
    }
}