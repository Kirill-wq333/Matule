package com.example.domain.ui.presentation.feature.cart.model

data class CartState(
    val items: List<CartItem> = emptyList(),
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CartResult {
    data class Success(val cartItem: CartItem? = null, val items: List<CartItem> = emptyList()) : CartResult()
    data class Error(val message: String) : CartResult()
}