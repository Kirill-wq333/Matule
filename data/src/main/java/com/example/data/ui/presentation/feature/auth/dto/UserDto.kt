package com.example.data.ui.presentation.feature.auth.dto

import com.example.domain.ui.presentation.feature.auth.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("country") val country: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("createdAt") val createdAt: String
) {
    fun toUser(): User = User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        country = country,
        avatar = avatar,
        createdAt = createdAt
    )
}