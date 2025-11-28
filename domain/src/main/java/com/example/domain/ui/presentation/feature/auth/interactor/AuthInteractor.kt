package com.example.domain.ui.presentation.feature.auth.interactor

import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import kotlinx.coroutines.runBlocking

class AuthInteractor(
    private val authRepository: AuthRepository,
) {
    suspend fun login(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
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

    suspend fun logout() {
        authRepository.clearTokens()
    }
}