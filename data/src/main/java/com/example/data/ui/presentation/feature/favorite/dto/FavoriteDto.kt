package com.example.data.ui.presentation.feature.favorite.dto

import com.example.data.ui.presentation.feature.favorite.dto.FavoriteProductDto.Companion.toFavorite
import com.example.domain.ui.presentation.feature.favorite.model.Favorite
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteProduct

data class FavoriteDto(
    val id: Long,
    val userId: Long,
    val productId: Long,
    val createdAt: String,
    val product: FavoriteProductDto
){

    companion object{
        fun FavoriteDto.toFavorite() : Favorite =
            Favorite(
                id = id,
                userId = userId,
                productId = productId,
                createdAt = createdAt,
                product = product.toFavorite()
            )
    }

}

data class FavoriteProductDto(
    val id : Long,
    val name: String,
    val images: List<String>,
    val price: Double
){
    companion object{
        fun FavoriteProductDto.toFavorite(): FavoriteProduct =
            FavoriteProduct(
                id = id,
                name = name,
                images = images,
                price = price
            )
    }
}
