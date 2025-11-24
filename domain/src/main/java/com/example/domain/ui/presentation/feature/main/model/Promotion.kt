package com.example.domain.ui.presentation.feature.main.model

import kotlinx.serialization.Serializable

@Serializable
data class Promotion(
    val id: Long,
    val image: String,
    val validUntil: String,
    val isActive: Boolean,
    val createdAt: String
)
