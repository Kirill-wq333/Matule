package com.example.data.ui.presentation.feature.profile.datasource

import com.example.data.ui.presentation.feature.profile.dto.UserProfileDto
import com.example.data.ui.presentation.feature.profile.dto.request.UpdateProfileRequest
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApiService {

    @WithAuthorization
    @GET("profile")
    suspend fun getProfile(): UserProfileDto

    @WithAuthorization
    @PUT("profile")
    suspend fun updateProfile(@Body profileDto: UpdateProfileRequest): UserProfileDto
}