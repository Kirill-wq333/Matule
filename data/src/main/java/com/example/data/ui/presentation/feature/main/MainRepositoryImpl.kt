package com.example.data.ui.presentation.feature.main

import com.example.data.ui.presentation.feature.main.datasourse.MainApiService
import com.example.data.ui.presentation.feature.main.dto.CategoryDto.Companion.toCategory
import com.example.data.ui.presentation.feature.main.dto.ProductDto.Companion.toProductDto
import com.example.data.ui.presentation.feature.main.dto.PromotionDto.Companion.toPromotion
import com.example.domain.ui.presentation.feature.main.model.Category
import com.example.domain.ui.presentation.feature.main.model.FavoriteResult
import com.example.domain.ui.presentation.feature.main.model.Product
import com.example.domain.ui.presentation.feature.main.model.Promotion
import com.example.domain.ui.presentation.feature.main.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val apiService: MainApiService
) : MainRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            Result.success(response.map { it.toCategory() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPromotions(): Result<List<Promotion>> {
        return try {
            val response = apiService.getPromotions()
            Result.success(response.map { it.toPromotion() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            Result.success(response.map { it.toProductDto() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(productId: Long): Result<Product> {
        return try {
            val response = apiService.getProductById(productId)
            Result.success(response.toProductDto())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val response = apiService.getProductsByCategory(category)
            Result.success(response.map { it.toProductDto() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPopularProducts(): Result<List<Product>> {
        return try {
            val allProducts = apiService.getProducts()
            val popularProducts = allProducts.filter { it.isPopular }
            Result.success(popularProducts.map { it.toProductDto() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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