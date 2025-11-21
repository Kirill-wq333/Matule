package com.example.domain.ui.presentation.feature.auth.repository

import com.example.domain.ui.presentation.feature.auth.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun getCurrentUser(): User?
}