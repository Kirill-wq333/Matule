package com.example.data.ui.presentation.feature.popular.datasource

import com.example.data.ui.presentation.feature.popular.dto.ProductDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.GET
import retrofit2.http.Path

interface PopularApiService {

    @WithAuthorization
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @WithAuthorization
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Long): ProductDto

    @WithAuthorization
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductDto>

}