package com.example.domain.ui.presentation.feature.register.repository

import com.example.domain.ui.presentation.feature.auth.model.User

interface RegisterRepository {
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String?,
        phone: String?
    ): Result<User>
}