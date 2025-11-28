package com.example.domain.ui.presentation.feature.favorite.model

sealed class FavoriteResult {
    data class Success(val productId: Long, val isFavorite: Boolean) : FavoriteResult()
    data class Error(val message: String) : FavoriteResult()
}