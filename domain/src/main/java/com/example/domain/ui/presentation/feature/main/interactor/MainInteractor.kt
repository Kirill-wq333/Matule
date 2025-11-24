package com.example.domain.ui.presentation.feature.main.interactor

import com.example.domain.ui.presentation.feature.main.model.Category
import com.example.domain.ui.presentation.feature.main.model.FavoriteResult
import com.example.domain.ui.presentation.feature.main.model.HomeContent
import com.example.domain.ui.presentation.feature.main.model.Product
import com.example.domain.ui.presentation.feature.main.model.Promotion
import com.example.domain.ui.presentation.feature.main.repository.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MainInteractor(
    private val repository: MainRepository
) {

    suspend fun loadCategories(): Result<List<Category>> {
        return repository.getCategories()
    }

    suspend fun loadPromotions(): Result<List<Promotion>> {
        return repository.getPromotions()
    }

    suspend fun loadAllProducts(): Result<List<Product>> {
        return repository.getProducts()
    }

    suspend fun loadProductById(productId: Long): Result<Product> {
        return repository.getProductById(productId)
    }

    suspend fun loadProductsByCategory(category: String): Result<List<Product>> {
        return repository.getProductsByCategory(category)
    }

    suspend fun loadPopularProducts(): Result<List<Product>> {
        return repository.getPopularProducts()
    }

    suspend fun toggleFavorite(
        productId: Long,
        currentlyFavorite: Boolean
    ): Result<FavoriteResult> {
        return try {
            if (currentlyFavorite) {
                repository.removeFromFavorites(productId)
            } else {
                repository.addToFavorites(productId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadHomeContent(): Result<HomeContent> {
        return try {

            // Исправленный вариант с корутинами
            val categoriesResult = repository.getCategories()
            val promotionsResult = repository.getPromotions()
            val popularProductsResult = repository.getPopularProducts()

            // Проверяем все результаты
            if (categoriesResult.isSuccess && promotionsResult.isSuccess && popularProductsResult.isSuccess) {
                val homeContent = HomeContent(
                    categories = categoriesResult.getOrNull() ?: emptyList(),
                    popularProducts = popularProductsResult.getOrNull() ?: emptyList(),
                    promotions = promotionsResult.getOrNull() ?: emptyList()
                )
                Result.success(homeContent)
            } else {
                val error = categoriesResult.exceptionOrNull()
                    ?: promotionsResult.exceptionOrNull()
                    ?: popularProductsResult.exceptionOrNull()
                    ?: Exception("Unknown error")
                Result.failure(error)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Альтернативный вариант с параллельной загрузкой
    suspend fun loadHomeContentParallel(): Result<HomeContent> = coroutineScope {
        try {
            println("🏠 Loading home content in parallel...")

            // Параллельная загрузка с async
            val categoriesDeferred = async { repository.getCategories() }
            val promotionsDeferred = async { repository.getPromotions() }
            val popularProductsDeferred = async { repository.getPopularProducts() }

            // Ждем завершения всех задач
            val categories = categoriesDeferred.await()
            val promotions = promotionsDeferred.await()
            val popularProducts = popularProductsDeferred.await()

            // Проверяем все результаты
            if (categories.isSuccess && promotions.isSuccess && popularProducts.isSuccess) {
                val homeContent = HomeContent(
                    categories = categories.getOrNull() ?: emptyList(),
                    popularProducts = popularProducts.getOrNull() ?: emptyList(),
                    promotions = promotions.getOrNull() ?: emptyList()
                )
                println("✅ Home content loaded successfully in parallel")
                Result.success(homeContent)
            } else {
                val error = categories.exceptionOrNull()
                    ?: promotions.exceptionOrNull()
                    ?: popularProducts.exceptionOrNull()
                    ?: Exception("Unknown error")
                println("❌ Failed to load home content in parallel: ${error.message}")
                Result.failure(error)
            }

        } catch (e: Exception) {
            println("❌ Home content parallel loading error: ${e.message}")
            Result.failure(e)
        }
    }
}
