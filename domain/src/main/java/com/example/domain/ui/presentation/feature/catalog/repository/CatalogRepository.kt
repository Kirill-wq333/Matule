package com.example.domain.ui.presentation.feature.catalog.repository

import com.example.domain.ui.presentation.feature.catalog.model.Category

interface CatalogRepository {
    suspend fun getCategories(): Result<List<Category>>
}