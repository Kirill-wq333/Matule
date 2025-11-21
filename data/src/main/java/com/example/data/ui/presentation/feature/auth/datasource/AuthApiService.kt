package com.example.data.ui.presentation.feature.auth.datasource

import com.example.data.ui.presentation.feature.auth.dto.request.LoginRequest
import com.example.data.ui.presentation.feature.auth.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

}