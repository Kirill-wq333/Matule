package com.example.data.ui.presentation.feature.auth.dto.request


data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String? = null,
    val phone: String? = null
)

data class ForgotPasswordRequest(
    val email: String
)