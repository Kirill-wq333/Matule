package com.example.domain.ui.presentation.feature.register.interactor

import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.domain.ui.presentation.feature.register.repository.RegisterRepository

class RegisterInteractor(
    private val registerRepository: RegisterRepository
) {

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String? = null,
        phone: String? = null
    ): Result<User> {
        return registerRepository.register(email, password, firstName, lastName, phone)
    }

}