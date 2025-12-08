package com.example.domain.ui.presentation.feature.cart.model

data class CartState(
    val items: List<CartItem> = emptyList(),
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0,
    val subtotal: Double = 0.0,
    val delivery: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedItems: Set<Long> = emptySet()
)

sealed class CartResult {
    data class Success(val cartItem: CartItem? = null, val items: List<CartItem> = emptyList()) : CartResult()
    data class Error(val message: String) : CartResult()
}