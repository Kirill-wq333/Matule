package com.example.data.ui.presentation.feature.auth

import android.content.SharedPreferences
import com.example.data.ui.presentation.feature.auth.datasource.AuthApiService
import com.example.data.ui.presentation.feature.auth.dto.request.LoginRequest
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val sharedPreferences: SharedPreferences,
    private val appPreferences: AppPreferencesImpl
) : AuthRepository {

    private companion object {
        const val USER_ID_KEY = "user_id"
        const val USER_EMAIL_KEY = "user_email"
        const val USER_NAME_KEY = "user_name"
        const val USER_FIRST_NAME_KEY = "user_first_name"
        const val USER_LAST_NAME_KEY = "user_last_name"
        const val USER_PHONE_KEY = "user_phone"
    }

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        val response = apiService.login(LoginRequest(email, password))


        if (response.success && response.user != null && response.token != null) {
            tokenProvider.saveToken(response.token)

            saveUser(response.user.toUser())

            Result.success(response.user.toUser())
        } else {
            val errorMessage = response.error ?: "Неизвестная ошибка авторизации"
            Result.failure(Exception(errorMessage))
        }

    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun saveToken(token: String) {
        tokenProvider.saveToken(token)
    }

    override suspend fun getToken(): String? {
        return tokenProvider.getToken()
    }

    override suspend fun getCurrentUser(): User? {
        val id = sharedPreferences.getLong(USER_ID_KEY, -1L)
        val email = sharedPreferences.getString(USER_EMAIL_KEY, null)

        if (id == -1L || email == null) {
            return null
        }

        return User(
            id = id,
            email = email,
            name = sharedPreferences.getString(USER_NAME_KEY, "") ?: "",
            firstName = sharedPreferences.getString(USER_FIRST_NAME_KEY, null),
            lastName = sharedPreferences.getString(USER_LAST_NAME_KEY, null),
            phone = sharedPreferences.getString(USER_PHONE_KEY, null)
        )
    }

    private fun saveUser(user: User) {
        with(sharedPreferences.edit()) {
            putLong(USER_ID_KEY, user.id)
            putString(USER_EMAIL_KEY, user.email)
            putString(USER_NAME_KEY, user.name)

            user.firstName?.let { putString(USER_FIRST_NAME_KEY, it) }
            user.lastName?.let { putString(USER_LAST_NAME_KEY, it) }
            user.phone?.let { putString(USER_PHONE_KEY, it) }

            apply()
        }
    }

    override suspend fun clearTokens() {
        appPreferences.clearAllUserData()
    }

}