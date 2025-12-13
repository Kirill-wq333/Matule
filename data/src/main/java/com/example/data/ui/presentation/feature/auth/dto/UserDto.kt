package com.example.data.ui.presentation.feature.auth.dto

import com.example.domain.ui.presentation.feature.auth.model.User

data class UserDto(
    val id: Long,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val country: String? = null,
    val avatar: String? = null,
    val createdAt: String? = null
) {
    fun toUser(): User = User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        name = "$firstName $lastName"
    )
}