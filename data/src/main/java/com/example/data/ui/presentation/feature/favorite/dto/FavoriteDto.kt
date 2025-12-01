package com.example.data.ui.presentation.feature.favorite.dto

import com.example.data.ui.presentation.feature.popular.dto.ProductDto
import com.example.data.ui.presentation.feature.popular.dto.ProductDto.Companion.toProductDto
import com.example.domain.ui.presentation.feature.favorite.model.Favorite
import com.example.domain.ui.presentation.feature.popular.model.Product

data class FavoriteDto(
    val id: Long,
    val userId: Long,
    val productId: Int,
    val createdAt: String,
    val product: ProductDto
){

    companion object{
        fun FavoriteDto.toFavorite() : Favorite =
            Favorite(
                id = id,
                userId = userId,
                productId = productId,
                createdAt = createdAt,
                product = product.toProductDto()
            )
    }

}
