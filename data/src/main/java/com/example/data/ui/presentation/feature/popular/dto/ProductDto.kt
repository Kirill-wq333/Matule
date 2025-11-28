package com.example.data.ui.presentation.feature.popular.dto

import com.example.domain.ui.presentation.feature.popular.model.Product

data class ProductDto(
    val id: Long,
    val name: String,
    val category: String,
    val price: Double,
    val images: List<String>,
    val isFavorite: Boolean,
    val isPopular: Boolean,
    val description: String,
    val createdAt: String
) {
    companion object {
        fun ProductDto.toProductDto(): Product =
            Product(
                id = id,
                name = name,
                category = category,
                price = price,
                images = images,
                isFavorite = isFavorite,
                description = description,
                isPopular = isPopular,
                createdAt = createdAt
            )
    }
}