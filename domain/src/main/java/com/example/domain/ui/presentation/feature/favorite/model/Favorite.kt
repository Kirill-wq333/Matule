package com.example.domain.ui.presentation.feature.favorite.model

import com.example.domain.ui.presentation.feature.popular.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: Long,
    val userId: Long,
    val productId: Int,
    val createdAt: String,
    val product: Product
)
