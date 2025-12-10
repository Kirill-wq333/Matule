package com.example.domain.ui.presentation.feature.popular.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val name: String = "",
    val category: String = "",
    val subcategory: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val images: List<String> = listOf(),
    val isPopular: Boolean = true,
    val createdAt: String = "",
    val isFavorite: Boolean = false
)