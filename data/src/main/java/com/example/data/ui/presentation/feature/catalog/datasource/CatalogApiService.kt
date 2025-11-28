package com.example.data.ui.presentation.feature.catalog.datasource

import com.example.data.ui.presentation.feature.catalog.dto.CategoryDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.GET

interface CatalogApiService {

    @WithAuthorization
    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

}