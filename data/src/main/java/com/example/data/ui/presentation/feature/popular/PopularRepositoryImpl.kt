package com.example.data.ui.presentation.feature.popular

import com.example.data.ui.presentation.feature.popular.datasource.PopularApiService
import com.example.data.ui.presentation.feature.popular.dto.ProductDto.Companion.toProductDto
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository
import javax.inject.Inject

class PopularRepositoryImpl @Inject constructor(
    private val apiService: PopularApiService
): PopularRepository{

    override suspend fun getProducts(): Result<List<Product>>  = runCatching {
        val response = apiService.getProducts()
        Result.success(response.map { it.toProductDto() })
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun getProductById(productId: Long): Result<Product> = runCatching {
        val response = apiService.getProductById(productId)
        Result.success(response.toProductDto())
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun getProductsByCategory(category: String): Result<List<Product>> = runCatching {
        val response = apiService.getProductsByCategory(category)
        Result.success(response.map { it.toProductDto() })
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

}