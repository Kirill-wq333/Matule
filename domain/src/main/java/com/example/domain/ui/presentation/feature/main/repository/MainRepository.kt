package com.example.domain.ui.presentation.feature.main.repository

import com.example.domain.ui.presentation.feature.main.model.Category
import com.example.domain.ui.presentation.feature.main.model.FavoriteResult
import com.example.domain.ui.presentation.feature.main.model.Product
import com.example.domain.ui.presentation.feature.main.model.Promotion


interface MainRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getPromotions(): Result<List<Promotion>>
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductById(productId: Long): Result<Product>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
    suspend fun getPopularProducts(): Result<List<Product>>
    suspend fun addToFavorites(productId: Long): Result<FavoriteResult>
    suspend fun removeFromFavorites(productId: Long): Result<FavoriteResult>
}