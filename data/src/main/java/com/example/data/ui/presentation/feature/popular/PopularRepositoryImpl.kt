package com.example.data.ui.presentation.feature.popular

import com.example.data.ui.presentation.feature.popular.dto.ProductDto.Companion.toProductDto
import com.example.data.ui.presentation.feature.popular.datasource.PopularApiService
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository
import javax.inject.Inject

class PopularRepositoryImpl @Inject constructor(
    private val apiService: PopularApiService
): PopularRepository{

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

}