package com.example.data.ui.presentation.feature.auth.dto

import com.example.domain.ui.presentation.feature.auth.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null
) {
    fun toUser(): User = User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        name = "$firstName $lastName"
    )
}