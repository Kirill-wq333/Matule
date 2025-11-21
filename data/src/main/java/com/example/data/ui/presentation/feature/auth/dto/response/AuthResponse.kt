package com.example.data.ui.presentation.feature.auth.dto.response

import com.example.data.ui.presentation.feature.auth.dto.UserDto
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("user") val user: UserDto?,
    @SerializedName("token") val token: String?,
    @SerializedName("error") val error: String?
)