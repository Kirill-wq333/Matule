package com.example.domain.ui.presentation.feature.main.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val name: String,
    val category: String,
    val price: Double,
    val description: String,
    val images: List<String>,
    val isPopular: Boolean,
    val createdAt: String,
    val isFavorite: Boolean
)
