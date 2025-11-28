package com.example.domain.ui.presentation.feature.cart.model

import com.example.domain.ui.presentation.feature.popular.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val product: Product? = null
)