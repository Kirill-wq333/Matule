package com.example.domain.ui.presentation.feature.favorite.repository

import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult

interface FavoriteRepository {
    suspend fun addToFavorites(productId: Long): Result<FavoriteResult>
    suspend fun removeFromFavorites(productId: Long): Result<FavoriteResult>
}