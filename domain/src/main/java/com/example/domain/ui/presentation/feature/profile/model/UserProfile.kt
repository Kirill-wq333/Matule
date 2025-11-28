package com.example.domain.ui.presentation.feature.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
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
)
