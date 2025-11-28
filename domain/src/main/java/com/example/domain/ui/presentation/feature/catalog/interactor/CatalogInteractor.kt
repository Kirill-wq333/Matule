package com.example.domain.ui.presentation.feature.catalog.interactor

import com.example.domain.ui.presentation.feature.catalog.model.Category
import com.example.domain.ui.presentation.feature.catalog.repository.CatalogRepository

class CatalogInteractor(
    private val catalogRepository: CatalogRepository
) {
    suspend fun loadCategories(): Result<List<Category>> {
        return catalogRepository.getCategories()
    }
}