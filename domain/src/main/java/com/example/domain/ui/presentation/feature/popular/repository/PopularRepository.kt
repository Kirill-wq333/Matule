package com.example.domain.ui.presentation.feature.popular.repository

import com.example.domain.ui.presentation.feature.popular.model.Product

interface PopularRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductById(productId: Long): Result<Product>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
}