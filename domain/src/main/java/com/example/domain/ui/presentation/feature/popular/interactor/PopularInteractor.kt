package com.example.domain.ui.presentation.feature.popular.interactor

import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.domain.ui.presentation.feature.popular.repository.PopularRepository

class PopularInteractor(
    private val popularRepository: PopularRepository
) {

    suspend fun loadProductsByCategory(category: String): Result<List<Product>> {
        return popularRepository.getProductsByCategory(category)
    }

    suspend fun loadAllProducts(): Result<List<Product>> {
        return popularRepository.getProducts()
    }

    suspend fun loadProductById(productId: Long): Result<Product> {
        return popularRepository.getProductById(productId)
    }
}