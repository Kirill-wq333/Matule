package com.example.data.ui.presentation.feature.auth.dto.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("phone") val phone: String? = null
)

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)