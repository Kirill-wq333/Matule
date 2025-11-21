package com.example.domain.ui.presentation.feature.auth.model

import com.google.gson.annotations.SerializedName


data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val country: String,
    val avatar: String,
    val createdAt: String
)
