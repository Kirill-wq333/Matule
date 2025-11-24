package com.example.data.ui.presentation.feature.auth.datasource

import com.example.data.ui.presentation.feature.auth.dto.request.ForgotPasswordRequest
import com.example.data.ui.presentation.feature.auth.dto.request.LoginRequest
import com.example.data.ui.presentation.feature.auth.dto.request.RegisterRequest
import com.example.data.ui.presentation.feature.auth.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): AuthResponse

}