package com.example.data.ui.presentation.feature.favorite

import com.example.data.ui.presentation.feature.favorite.datasource.FavoriteApiService
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.favorite.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val apiService: FavoriteApiService
): FavoriteRepository {

    override suspend fun addToFavorites(productId: Long): Result<FavoriteResult> {
        return try {
            val response = apiService.addToFavorites(productId)
            if (response.success) {
                Result.success(FavoriteResult.Success(productId, true))
            } else {
                Result.success(FavoriteResult.Error(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromFavorites(productId: Long): Result<FavoriteResult> {
        return try {
            val response = apiService.removeFromFavorites(productId)
            if (response.success) {
                Result.success(FavoriteResult.Success(productId, false))
            } else {
                Result.success(FavoriteResult.Error(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}