package com.example.data.ui.presentation.feature.cart

import com.example.data.ui.presentation.feature.cart.datasource.CartApiService
import com.example.data.ui.presentation.feature.cart.dto.ProductCartDto.Companion.toProductCart
import com.example.data.ui.presentation.feature.cart.dto.request.AddToCartRequest
import com.example.data.ui.presentation.feature.cart.dto.request.UpdateCartItemRequest
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.domain.ui.presentation.feature.cart.model.ProductCart
import com.example.domain.ui.presentation.feature.cart.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
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


    override suspend fun getCart(): Result<CartState> = runCatching {
        val response = apiService.getCart()

        if (response.success) {
            val items = response.items?.map { itemDto ->
                CartItem(
                    id = itemDto.id,
                    productId = itemDto.productId,
                    quantity = itemDto.quantity,
                    createdAt = itemDto.createdAt,
                    product = itemDto.product?.toProductCart()
                )
            } ?: emptyList()

            val summary = response.summary
            val cartState = CartState(
                items = items,
                totalItems = summary?.itemsCount ?: items.sumOf { it.quantity },
                totalPrice = summary?.total ?: 0.0,
                subtotal = summary?.subtotal ?: 0.0,
                delivery = summary?.delivery ?: 0.0
            )

            Result.success(cartState)
        } else {
            Result.failure(Exception("Failed to load cart: ${response.message}"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun addToCart(productId: Long, quantity: Int): Result<CartResult> =
        runCatching {
            val token = tokenProvider.getToken()
                ?: return Result.failure(Exception("Пользователь не авторизован"))

            val response = apiService.addToCart(AddToCartRequest(productId, quantity))

            if (response.success) {
                val cartItem = response.items?.firstOrNull { it.productId == productId }
                    ?.let { itemDto ->
                        CartItem(
                            id = itemDto.id,
                            productId = itemDto.productId,
                            quantity = itemDto.quantity,
                            createdAt = itemDto.createdAt,
                            product = itemDto.product?.let { productDto ->
                                ProductCart(
                                    id = productDto.id,
                                    name = productDto.name,
                                    price = productDto.price,
                                    images = productDto.images,
                                    description = productDto.description,
                                )
                            }
                        )
                    }

                Result.success(CartResult.Success(cartItem = cartItem))
            } else {
                Result.success(
                    CartResult.Error(
                        response.message ?: "Не удалось добавить в корзину"
                    )
                )
            }
        }.fold(
            onSuccess = { it },
            onFailure = { Result.failure(it) }
        )

    override suspend fun updateCartItem(cartItemId: Long, quantity: Int): Result<CartResult> =
        runCatching {
            val response = apiService.updateCartItem(cartItemId, UpdateCartItemRequest(quantity))

            if (response.success) {
                val cartItem = response.items?.firstOrNull { it.id == cartItemId }
                    ?.let { itemDto ->
                        CartItem(
                            id = itemDto.id,
                            productId = itemDto.productId,
                            quantity = itemDto.quantity,
                            createdAt = itemDto.createdAt,
                            product = itemDto.product?.let { productDto ->
                                ProductCart(
                                    id = productDto.id,
                                    name = productDto.name,
                                    price = productDto.price,
                                    images = productDto.images,
                                    description = productDto.description
                                )
                            }
                        )
                    }

                Result.success(CartResult.Success(cartItem = cartItem))
            } else {
                Result.success(CartResult.Error(response.message ?: "Не удалось обновить товар"))
            }
        }.fold(
            onSuccess = { it },
            onFailure = { Result.failure(it) }
        )

    override suspend fun removeFromCart(cartItemId: Long): Result<CartResult> =
        runCatching {
            val response = apiService.removeFromCart(cartItemId)

            if (response.success) {
                getCart().fold(
                    onSuccess = { cartState ->
                        val productIds = cartState.items.map { it.productId }.toSet()
                        saveLocalCartItems(productIds)

                        Result.success(CartResult.Success(items = cartState.items))
                    },
                    onFailure = { Result.failure(it) }
                )
            } else {
                Result.success(CartResult.Error(response.message ?: "Не удалось удалить товар"))
            }
        }.fold(
            onSuccess = { it },
            onFailure = { Result.failure(it) }
        )


}