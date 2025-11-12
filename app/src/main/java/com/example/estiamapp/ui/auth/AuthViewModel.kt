package com.example.estiamapp.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    val currentUser get() = auth.currentUser

    fun login(email: String, password: String, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _isAuthenticated.value = true }
            .addOnFailureListener { onError(it.message ?: "Login failure!") }
    }

    fun register(email: String, password: String, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { _isAuthenticated.value = true }
            .addOnFailureListener { onError(it.message ?: "Register failure!") }
    }

    fun logout() {
        auth.signOut()
        _isAuthenticated.value = false
    }
}