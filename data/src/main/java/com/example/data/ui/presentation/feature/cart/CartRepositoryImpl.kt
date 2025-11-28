package com.example.data.ui.presentation.feature.cart

import com.example.data.ui.presentation.feature.cart.datasource.CartApiService
import com.example.data.ui.presentation.feature.cart.dto.CartItemDto.Companion.toCartItem
import com.example.data.ui.presentation.feature.cart.dto.request.AddToCartRequest
import com.example.data.ui.presentation.feature.cart.dto.request.UpdateCartItemRequest
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val popularRepository: PopularRepository,
    private val apiService: CartApiService,
    private val tokenProvider: TokenProvider,
    private val appPreferences: AppPreferencesImpl
) : CartRepository {

    override suspend fun getLocalCartItems(): Set<Long> {
        return appPreferences.getCartItems()
    }

    override suspend fun saveLocalCartItems(productIds: Set<Long>) {
        appPreferences.setCartItems(productIds)
    }

    override suspend fun syncCartWithServer(): Result<Boolean> {
        return try {
            val localItems = getLocalCartItems()

            localItems.forEach { productId ->
                val result = addToCart(productId, 1)

            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getCart(): Result<CartState> {
        return try {
            val response = apiService.getCart()

            if (response.success) {
                val cartItemsDto = response.cartItems ?: emptyList()

                val cartItems: List<CartItem> = cartItemsDto.map { it.toCartItem() }

                val enrichedCartItems = enrichCartItemsWithProducts(cartItems)

                val totalItems = enrichedCartItems.sumOf { it.quantity }
                val totalPrice = enrichedCartItems.sumOf {
                    it.product?.price?.times(it.quantity) ?: 0.0
                }

                val cartState = CartState(
                    items = enrichedCartItems,
                    totalItems = totalItems,
                    totalPrice = totalPrice
                )

                Result.success(cartState)
            } else {
                Result.failure(Exception(response.message ?: "Failed to load cart"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun enrichCartItemsWithProducts(cartItems: List<CartItem>): List<CartItem> {
        return cartItems.map { cartItem ->
            if (cartItem.product == null) {
                try {
                    val productResult = popularRepository.getProductById(cartItem.productId)
                    if (productResult.isSuccess) {
                        cartItem.copy(product = productResult.getOrNull())
                    } else {
                        cartItem
                    }
                } catch (e: Exception) {
                    cartItem
                }
            } else {
                cartItem
            }
        }
    }

    override suspend fun addToCart(productId: Long, quantity: Int): Result<CartResult> {
        return try {

            val token = tokenProvider.getToken()
            if (token == null) {
                return Result.failure(Exception("User not authenticated"))
            }

            val response = apiService.addToCart(
                AddToCartRequest(productId, quantity)
            )

            if (response.success) {
                val cartItem = response.cartItem?.toCartItem()
                Result.success(CartResult.Success(cartItem = cartItem))
            } else {
                Result.success(CartResult.Error(response.message ?: "Failed to add to cart"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    override suspend fun updateCartItem(cartItemId: Long, quantity: Int): Result<CartResult> {
        return try {
            val response = apiService.updateCartItem(cartItemId, UpdateCartItemRequest(quantity))
            
            if (response.success) {
                val cartItem = response.cartItem?.toCartItem()
                Result.success(CartResult.Success(cartItem = cartItem))
            } else {
                Result.success(CartResult.Error(response.message ?: "Failed to update cart item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeFromCart(cartItemId: Long): Result<CartResult> {
        return try {
            val response = apiService.removeFromCart(cartItemId)
            
            if (response.success) {
                Result.success(CartResult.Success())
            } else {
                Result.success(CartResult.Error(response.message ?: "Failed to remove from cart"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    

}