package com.example.data.ui.presentation.feature.profile.dto.request

data class UpdateProfileRequest(
    val email: String,
    val firstName: String,
    val lastName: String? = null,
    val phone: String? = null,
    val country: String? = null,
    val city: String? = null,
    val address: String? = null,
    val postalCode: String? = null,
    val dateOfBirth: String? = null
)