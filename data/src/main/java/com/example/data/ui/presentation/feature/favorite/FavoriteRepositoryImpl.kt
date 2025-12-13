package com.example.data.ui.presentation.feature.favorite

import com.example.data.ui.presentation.feature.favorite.datasource.FavoriteApiService
import com.example.data.ui.presentation.feature.favorite.dto.FavoriteDto.Companion.toFavorite
import com.example.domain.ui.presentation.feature.favorite.model.Favorite
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.favorite.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val apiService: FavoriteApiService
): FavoriteRepository {

    override suspend fun getFavorite(): Result<List<Favorite>> = runCatching {
        val response = apiService.getFavorites()

        val favorite = response.map { it.toFavorite() }
        Result.success(favorite)
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun addToFavorites(productId: Long): Result<FavoriteResult> = runCatching {
        val response = apiService.addToFavorites(productId)
        if (response.success) {
            Result.success(FavoriteResult.Success(productId, true))
        } else {
            Result.success(FavoriteResult.Error(response.message ?: "Unknown error"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun removeFromFavorites(productId: Long): Result<FavoriteResult> = runCatching {
        val response = apiService.removeFromFavorites(productId)
        if (response.success) {
            Result.success(FavoriteResult.Success(productId, false))
        } else {
            Result.success(FavoriteResult.Error(response.message ?: "Unknown error"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

}