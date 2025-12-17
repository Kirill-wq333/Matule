package com.example.data.ui.presentation.feature.register

import android.content.SharedPreferences
import com.example.data.ui.presentation.feature.auth.dto.request.RegisterRequest
import com.example.data.ui.presentation.feature.register.datasource.RegisterApiService
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.domain.ui.presentation.feature.register.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val apiService: RegisterApiService,
    private val tokenProvider: TokenProvider,
    private val sharedPreferences: SharedPreferences
) : RegisterRepository {

    private companion object {
        const val USER_ID_KEY = "user_id"
        const val USER_EMAIL_KEY = "user_email"
        const val USER_NAME_KEY = "user_name"
        const val USER_FIRST_NAME_KEY = "user_first_name"
        const val USER_LAST_NAME_KEY = "user_last_name"
        const val USER_PHONE_KEY = "user_phone"

    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
    ): Result<User> = runCatching {

            val response = apiService.register(RegisterRequest(email, password, firstName))

            if (response.success && response.user != null && response.token != null) {
                tokenProvider.saveToken(response.token)
                saveUser(response.user.toUser())

                Result.success(response.user.toUser())
            } else {
                val errorMessage = when {
                    else -> "Неизвестная ошибка регистрации"
                }
                Result.failure(Exception(errorMessage))
            }

    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    private fun saveUser(user: User) {
        with(sharedPreferences.edit()) {
            putLong(USER_ID_KEY, user.id)
            putString(USER_EMAIL_KEY, user.email)
            putString(USER_NAME_KEY, user.name)

            user.firstName?.let { putString(USER_FIRST_NAME_KEY, it) }
            apply()
        }
    }

}
