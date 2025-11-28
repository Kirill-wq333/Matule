package com.example.domain.ui.presentation.feature.main.interactor

import com.example.domain.ui.presentation.feature.arrivals.repository.ArrivalsRepository
import com.example.domain.ui.presentation.feature.catalog.repository.CatalogRepository
import com.example.domain.ui.presentation.feature.main.model.HomeContent
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository

class MainInteractor(
    private val catalogRepository: CatalogRepository,
    private val arrivalsRepository: ArrivalsRepository,
    private val popularRepository: PopularRepository,
) {

    suspend fun loadHomeContent(): Result<HomeContent> {
        return try {

            val categoriesResult = catalogRepository.getCategories()
            val promotionsResult = arrivalsRepository.getPromotions()
            val popularProductsResult = popularRepository.getProducts()

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
}
