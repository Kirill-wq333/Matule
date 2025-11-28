package com.example.data.ui.presentation.feature.profile

import com.example.data.ui.presentation.feature.profile.datasource.ProfileApiService
import com.example.data.ui.presentation.feature.profile.dto.UserProfileDto.Companion.toUserProfile
import com.example.data.ui.presentation.feature.profile.dto.request.UpdateProfileRequest
import com.example.domain.ui.presentation.feature.profile.model.ProfileResult
import com.example.domain.ui.presentation.feature.profile.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService
): ProfileRepository {

    override suspend fun getProfile(): Result<ProfileResult> {
        return try {
            val response = apiService.getProfile()
            Result.success(ProfileResult.Success(response.toUserProfile()))
        } catch (e: Exception) {
            Result.success(ProfileResult.Error("Не удалось загрузить профиль: ${e.message}"))
        }
    }

    override suspend fun updateProfileFields(
        email: String,
        firstName: String,
        lastName: String?,
        phone: String?,
        country: String?,
        city: String?,
        address: String?,
        postalCode: String?,
        dateOfBirth: String?
    ): Result<ProfileResult> {
        return try {
            val request = UpdateProfileRequest(
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
            val response = apiService.updateProfile(request)
            Result.success(ProfileResult.Success(response.toUserProfile()))
        } catch (e: Exception) {
            Result.success(ProfileResult.Error("Не удалось обновить профиль: ${e.message}"))
        }
    }
}