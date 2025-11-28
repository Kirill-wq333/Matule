package com.example.domain.ui.presentation.feature.favorite.interactor

import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.favorite.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FavoriteInteractor(
    private val favoriteRepository: FavoriteRepository
) {
    private val _favoriteUpdates = MutableSharedFlow<Pair<Long, Boolean>>()
    val favoriteUpdates = _favoriteUpdates.asSharedFlow()

    private val favoriteCache = mutableMapOf<Long, Boolean>()

    suspend fun toggleFavorite(
        productId: Long,
        currentlyFavorite: Boolean
    ): Result<FavoriteResult> {
        return try {
            val newFavoriteStatus = !currentlyFavorite

            favoriteCache[productId] = newFavoriteStatus
            if (currentlyFavorite) {
                favoriteRepository.removeFromFavorites(productId)
            } else {
                favoriteRepository.addToFavorites(productId)
            }

            _favoriteUpdates.emit(productId to newFavoriteStatus)

            Result.success(FavoriteResult.Success(productId, newFavoriteStatus))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}