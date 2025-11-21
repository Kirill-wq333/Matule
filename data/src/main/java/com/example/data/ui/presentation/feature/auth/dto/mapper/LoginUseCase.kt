package com.example.data.ui.presentation.feature.auth.dto.mapper

import com.example.domain.ui.presentation.feature.auth.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<com.example.domain.ui.presentation.feature.auth.model.User> {
        return repository.login(email, password)
    }
}