package com.example.data.ui.presentation.feature.cart.dto.response

import com.example.data.ui.presentation.feature.cart.dto.CartItemDto

data class CartResponseDto(
    val success: Boolean,
    val message: String? = null,
    val items: List<CartItemDto>? = null,
    val summary: CartSummaryDto? = null
)
data class CartSummaryDto(
    val subtotal: Double,
    val delivery: Double,
    val total: Double,
    val itemsCount: Int
)