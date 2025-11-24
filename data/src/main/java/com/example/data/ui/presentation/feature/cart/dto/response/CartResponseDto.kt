package com.example.data.ui.presentation.feature.cart.dto.response

import com.example.data.ui.presentation.feature.cart.dto.CartItemDto
import com.google.gson.annotations.SerializedName

data class CartResponseDto(
     val success: Boolean,
     val message: String? = null,
     val cartItem: CartItemDto? = null,
     val cartItems: List<CartItemDto>? = null
)