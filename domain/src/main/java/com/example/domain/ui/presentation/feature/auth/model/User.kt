package com.example.domain.ui.presentation.feature.auth.model

import com.google.gson.annotations.SerializedName


data class User(
    val id: Long,
    val email: String,
    val name: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null
)
