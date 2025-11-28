package com.example.domain.ui.presentation.feature.profile.interactor

import com.example.domain.ui.presentation.feature.profile.model.ProfileResult
import com.example.domain.ui.presentation.feature.profile.repository.ProfileRepository

class ProfileInteractor(
    private val profileRepository: ProfileRepository
) {

    suspend fun getProfile(): Result<ProfileResult> {
        return profileRepository.getProfile()
    }

    suspend fun updateProfileFields(
        email: String,
        firstName: String,
        lastName: String? = null,
        phone: String? = null,
        country: String? = null,
        city: String? = null,
        address: String? = null,
        postalCode: String? = null,
        dateOfBirth: String? = null
    ): Result<ProfileResult> {
        return profileRepository.updateProfileFields(
            email = email,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            country = country,
            city = city,
            address = address,
            postalCode = postalCode,
            dateOfBirth = dateOfBirth
        )
    }
}