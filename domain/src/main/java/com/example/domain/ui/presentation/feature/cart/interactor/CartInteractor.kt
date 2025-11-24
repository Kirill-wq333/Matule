package com.example.domain.ui.presentation.feature.cart.interactor

import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import javax.inject.Inject

class CartInteractor @Inject constructor(
    private val cartRepository: CartRepository
) {
    
    suspend fun getCart(): Result<CartState> {
        return cartRepository.getCart()
    }

    suspend fun getLocalCartItems(): Set<Long> {
        return cartRepository.getLocalCartItems()
    }

    suspend fun saveLocalCartItems(productIds: Set<Long>) {
        cartRepository.saveLocalCartItems(productIds)
    }

    suspend fun syncCartWithServer(): Result<Boolean> {
        return cartRepository.syncCartWithServer()
    }

    suspend fun addToCartSimple(productId: Long, quantity: Int = 1): Result<Boolean> {
        return try {

            val currentItems = getLocalCartItems()
            val updatedItems = currentItems + productId
            saveLocalCartItems(updatedItems)

            val response = cartRepository.addToCart(productId, quantity)

            if (response.isSuccess) {
                val cartResult = response.getOrNull()!!
                when (cartResult) {
                    is CartResult.Success -> {
                        Result.success(true)
                    }
                    is CartResult.Error -> {
                        Result.success(true)
                    }
                }
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.success(true)
        }
    }
    
    suspend fun updateCartItem(cartItemId: Long, quantity: Int): Result<CartResult> {
        return cartRepository.updateCartItem(cartItemId, quantity)
    }
    
    suspend fun removeFromCart(cartItemId: Long): Result<CartResult> {
        return cartRepository.removeFromCart(cartItemId)
    }
    
}