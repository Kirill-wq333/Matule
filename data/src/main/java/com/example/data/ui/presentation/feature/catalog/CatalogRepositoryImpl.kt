package com.example.data.ui.presentation.feature.catalog

import com.example.data.ui.presentation.feature.catalog.datasource.CatalogApiService
import com.example.data.ui.presentation.feature.catalog.dto.CategoryDto.Companion.toCategory
import com.example.domain.ui.presentation.feature.catalog.model.Category
import com.example.domain.ui.presentation.feature.catalog.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: CatalogApiService
): CatalogRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            Result.success(response.map { it.toCategory() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}