package com.example.domain.ui.presentation.feature.profile.repository

import com.example.domain.ui.presentation.feature.profile.model.ProfileResult

interface ProfileRepository {
    suspend fun getProfile(): Result<ProfileResult>
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
    ): Result<ProfileResult>
}