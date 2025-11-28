package com.example.domain.ui.presentation.feature.forgot_password.repository

interface ForgotPasswordRepository {
    suspend fun forgotPassword(email: String): Result<Boolean>
}