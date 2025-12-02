package com.example.domain.ui.presentation.feature.favorite.model

import com.example.domain.ui.presentation.feature.popular.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: Long,
    val userId: Long,
    val productId: Long,
    val createdAt: String,
    val product: Product
)
//
//data class FavoriteProduct(
//    val id : Long,
//    val name: String,
//    val images: String,
//    val price: Double
//)
