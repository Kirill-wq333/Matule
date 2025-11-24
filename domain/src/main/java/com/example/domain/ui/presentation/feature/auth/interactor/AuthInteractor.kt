package com.example.domain.ui.presentation.feature.auth.interactor

import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository

class AuthInteractor(
    private val authRepository: AuthRepository
) {
    suspend fun login(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String? = null,
        phone: String? = null
    ): Result<User> {
        return authRepository.register(email, password, firstName, lastName, phone)
    }

    suspend fun forgotPassword(email: String): Result<Boolean> {
        return authRepository.forgotPassword(email)
    }

    suspend fun getToken(): String? {
        return authRepository.getToken()
    }

    suspend fun getCurrentUser(): User? {
        return authRepository.getCurrentUser()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.getToken() != null
    }
}