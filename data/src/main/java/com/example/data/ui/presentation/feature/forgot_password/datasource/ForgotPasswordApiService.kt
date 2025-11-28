package com.example.data.ui.presentation.feature.forgot_password.datasource

import com.example.data.ui.presentation.feature.auth.dto.request.ForgotPasswordRequest
import com.example.data.ui.presentation.feature.auth.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPasswordApiService {

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): AuthResponse

}