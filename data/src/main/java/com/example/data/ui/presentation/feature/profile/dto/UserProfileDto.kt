package com.example.data.ui.presentation.feature.profile.dto

import com.example.domain.ui.presentation.feature.profile.model.UserProfile

data class UserProfileDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String?,
    val phone: String?,
    val country: String?,
    val city: String?,
    val address: String?,
    val postalCode: String?,
    val avatar: String?,
    val dateOfBirth: String?,
    val createdAt: String,
    val updatedAt: String?
){
    companion object{
        fun UserProfileDto.toUserProfile(): UserProfile =
            UserProfile(
                id = id,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                country = country,
                city = city,
                address = address,
                postalCode = postalCode,
                avatar = avatar,
                dateOfBirth = dateOfBirth,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
    }
}
