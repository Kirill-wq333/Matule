package com.example.data.ui.presentation.feature.register.datasource

import com.example.data.ui.presentation.feature.auth.dto.request.RegisterRequest
import com.example.data.ui.presentation.feature.auth.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

}