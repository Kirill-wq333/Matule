package com.example.data.ui.presentation.feature.cart.dto

import com.example.data.ui.presentation.feature.cart.dto.ProductCartDto.Companion.toProductCart
import com.example.data.ui.presentation.feature.popular.dto.ProductDto
import com.example.data.ui.presentation.feature.popular.dto.ProductDto.Companion.toProductDto
import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.example.domain.ui.presentation.feature.cart.model.ProductCart
import com.google.gson.annotations.SerializedName

data class CartItemDto(
     val id: Long,
     val productId: Long,
     val quantity: Int,
     val createdAt: String,
     val product: ProductCartDto? = null
) {
    companion object {
        fun CartItemDto.toCartItem(): CartItem = CartItem(
            id = id,
            productId = productId,
            quantity = quantity,
            createdAt = createdAt,
            product = product?.toProductCart()
        )
    }
}

data class ProductCartDto(
    val id : Long,
    val name: String,
    val price: Double,
    val description: String,
    val images: List<String>,
){
    companion object{
        fun ProductCartDto.toProductCart(): ProductCart =
            ProductCart(
                id = id,
                name = name,
                price = price,
                description = description,
                images = images
            )
    }
}