package com.example.data.ui.presentation.feature.cart.dto.request

data class AddToCartRequest(
    val productId: Long,
    val quantity: Int = 1
)