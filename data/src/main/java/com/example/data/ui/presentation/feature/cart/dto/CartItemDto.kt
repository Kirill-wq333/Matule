package com.example.data.ui.presentation.feature.cart.dto

import com.example.data.ui.presentation.feature.popular.dto.ProductDto
import com.example.data.ui.presentation.feature.popular.dto.ProductDto.Companion.toProductDto
import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.google.gson.annotations.SerializedName

data class CartItemDto(
     val id: Long,
     val productId: Long,
     val quantity: Int,
     val product: ProductDto? = null
) {
    companion object {
        fun CartItemDto.toCartItem(): CartItem = CartItem(
            id = id,
            productId = productId,
            quantity = quantity,
            product = product?.toProductDto()
        )
    }
}