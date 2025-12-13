package com.example.data.ui.presentation.feature.auth.dto.response

import com.example.data.ui.presentation.feature.auth.dto.UserDto

data class AuthResponse(
    val success: Boolean,
    val user: UserDto?,
    val token: String?,
    val error: String?
)