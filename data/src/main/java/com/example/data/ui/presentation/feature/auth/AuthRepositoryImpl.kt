package com.example.data.ui.presentation.feature.auth

import android.content.Context
import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.data.ui.presentation.feature.auth.dto.request.LoginRequest
import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    private companion object {
        const val AUTH_TOKEN_KEY = "auth_token"
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.success && response.user != null && response.token != null) {
                saveToken(response.token)
                Result.success(response.user.toUser())
            } else {
                Result.failure(Exception(response.error ?: "Ошибка авторизации"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun logout() {
        with(sharedPreferences.edit()) {
            remove(AUTH_TOKEN_KEY)
            apply()
        }
    }

    override suspend fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(AUTH_TOKEN_KEY, token)
            apply()
        }
    }

    override suspend fun getToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null)
    }

    override suspend fun getCurrentUser(): User? {
        return null
    }
}