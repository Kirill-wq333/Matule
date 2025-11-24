package com.example.domain.ui.presentation.feature.main.model

data class HomeContent(
    val categories: List<Category>,
    val popularProducts: List<Product>,
    val promotions: List<Promotion>
)

sealed class FavoriteResult {
    data class Success(val productId: Long, val isFavorite: Boolean) : FavoriteResult()
    data class Error(val message: String) : FavoriteResult()
}