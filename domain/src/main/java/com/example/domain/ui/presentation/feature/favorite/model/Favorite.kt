package com.example.domain.ui.presentation.feature.favorite.model

import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: Long,
    val userId: Long,
    val productId: Long,
    val createdAt: String,
    val product: FavoriteProduct
)


data class FavoriteProduct(
    val id : Long,
    val name: String,
    val images: List<String>,
    val price: Double
)
