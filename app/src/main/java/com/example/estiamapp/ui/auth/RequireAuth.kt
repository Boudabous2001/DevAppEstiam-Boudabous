package com.example.estiamapp.ui.auth

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun RequireAuth (
    navController: NavController,
    content: @Composable () -> Unit
) {
    val vmAuth: AuthViewModel = viewModel()
    val isAuthed by vmAuth.isAuthenticated.collectAsState()

    if (!isAuthed) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    } else {
        content()
    }
}