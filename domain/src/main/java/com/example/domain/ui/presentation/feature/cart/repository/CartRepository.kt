package com.example.domain.ui.presentation.feature.cart.repository

import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.cart.model.CartState

interface CartRepository {
    suspend fun getCart(): Result<CartState>
    suspend fun addToCart(productId: Long, quantity: Int = 1): Result<CartResult>
    suspend fun updateCartItem(cartItemId: Long, quantity: Int): Result<CartResult>
    suspend fun removeFromCart(cartItemId: Long): Result<CartResult>
    suspend fun getLocalCartItems(): Set<Long>
    suspend fun saveLocalCartItems(productIds: Set<Long>)
    suspend fun syncCartWithServer(): Result<Boolean>
}