package com.example.data.ui.presentation.feature.forgot_password

import com.example.data.ui.presentation.feature.auth.dto.request.ForgotPasswordRequest
import com.example.data.ui.presentation.feature.forgot_password.datasource.ForgotPasswordApiService
import com.example.domain.ui.presentation.feature.forgot_password.repository.ForgotPasswordRepository
import javax.inject.Inject

class ForgotPasswordRepositoryImpl @Inject constructor(
    private val apiService: ForgotPasswordApiService
) : ForgotPasswordRepository {

    override suspend fun forgotPassword(email: String): Result<Boolean> {
        return try {
            val response = apiService.forgotPassword(ForgotPasswordRequest(email))
            Result.success(response.success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}