package com.example.domain.ui.presentation.feature.catalog.repository

import com.example.domain.ui.presentation.feature.catalog.model.Category
import com.example.domain.ui.presentation.feature.popular.model.Product

interface CatalogRepository {
    suspend fun getCategories(): Result<List<Category>>
}