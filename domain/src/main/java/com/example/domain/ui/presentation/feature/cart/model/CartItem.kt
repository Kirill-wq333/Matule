package com.example.domain.ui.presentation.feature.cart.model

import com.example.domain.ui.presentation.feature.popular.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val createdAt: String,
    val product: ProductCart? = null
)

data class ProductCart(
    val id: Long,
    val name: String,
    val price: Double,
    val description: String,
    val images: List<String>,
)