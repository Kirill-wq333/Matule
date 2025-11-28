package com.example.domain.ui.presentation.feature.forgot_password.interactor

import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.forgot_password.repository.ForgotPasswordRepository

class ForgotPasswordInteractor(
    private val forgotPasswordRepository: ForgotPasswordRepository
) {

    suspend fun forgotPassword(email: String): Result<Boolean> {
        return forgotPasswordRepository.forgotPassword(email)
    }

}