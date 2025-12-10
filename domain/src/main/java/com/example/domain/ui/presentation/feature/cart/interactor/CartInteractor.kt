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

    suspend fun getLocalCartItems(): Result<Set<Long>> {
        return runCatching {
            cartRepository.getLocalCartItems()
        }
    }

    suspend fun saveLocalCartItems(productIds: Set<Long>): Result<Boolean> {
        return runCatching {
            cartRepository.saveLocalCartItems(productIds)
            true
        }
    }

    suspend fun syncCartWithServer(): Result<Boolean> {
        return cartRepository.syncCartWithServer()
    }

    suspend fun addToCartSimple(productId: Long, quantity: Int = 1): Result<Boolean> {
        return runCatching {
            val currentItems = getLocalCartItems().getOrNull() ?: emptySet()

            val updatedItems = currentItems + productId
            saveLocalCartItems(updatedItems)

            val response = cartRepository.addToCart(productId, quantity)

            response.fold(
                onSuccess = { cartResult ->
                    when (cartResult) {
                        is CartResult.Success -> {
                            true
                        }
                        is CartResult.Error -> {
                            saveLocalCartItems(currentItems)
                            false
                        }
                    }
                },
                onFailure = { error ->
                    saveLocalCartItems(currentItems)
                    false
                }
            )
        }
    }
    
    suspend fun updateCartItem(cartItemId: Long, quantity: Int): Result<CartResult> {
        return cartRepository.updateCartItem(cartItemId, quantity)
    }
    
    suspend fun removeFromCart(cartItemId: Long): Result<CartResult> {
        return cartRepository.removeFromCart(cartItemId)
    }
    
}