package com.example.domain.ui.presentation.feature.main.interactor

import com.example.domain.ui.presentation.feature.arrivals.repository.ArrivalsRepository
import com.example.domain.ui.presentation.feature.main.model.HomeContent
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository

class MainInteractor(
    private val arrivalsRepository: ArrivalsRepository,
    private val popularRepository: PopularRepository,
) {

    suspend fun loadHomeContent(): Result<HomeContent> {
        return try {

            val promotionsResult = arrivalsRepository.getPromotions()
            val popularProductsResult = popularRepository.getProducts()

            if (promotionsResult.isSuccess && popularProductsResult.isSuccess) {
                val homeContent = HomeContent(
                    popularProducts = popularProductsResult.getOrNull() ?: emptyList(),
                    promotions = promotionsResult.getOrNull() ?: emptyList()
                )
                Result.success(homeContent)
            } else {
                val error = popularProductsResult.exceptionOrNull()
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
