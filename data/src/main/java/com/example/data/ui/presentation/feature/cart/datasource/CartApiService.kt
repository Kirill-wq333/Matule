package com.example.data.ui.presentation.feature.cart.datasource

import com.example.data.ui.presentation.feature.cart.dto.request.AddToCartRequest
import com.example.data.ui.presentation.feature.cart.dto.request.UpdateCartItemRequest
import com.example.data.ui.presentation.feature.cart.dto.response.CartResponseDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartApiService {

    @WithAuthorization
    @GET("cart")
    suspend fun getCart(): CartResponseDto

    @WithAuthorization
    @POST("cart")
    suspend fun addToCart(@Body request: AddToCartRequest): CartResponseDto

    @WithAuthorization
    @PUT("cart/{id}")
    suspend fun updateCartItem(@Path("id") cartItemId: Long, @Body request: UpdateCartItemRequest): CartResponseDto

    @WithAuthorization
    @DELETE("cart/{id}")
    suspend fun removeFromCart(@Path("id") cartItemId: Long): CartResponseDto
}