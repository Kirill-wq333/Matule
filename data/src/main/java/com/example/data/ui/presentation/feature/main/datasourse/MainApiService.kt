package com.example.data.ui.presentation.feature.main.datasourse

import com.example.data.ui.presentation.feature.main.dto.CategoryDto
import com.example.data.ui.presentation.feature.main.dto.ProductDto
import com.example.data.ui.presentation.feature.main.dto.PromotionDto
import com.example.data.ui.presentation.feature.main.dto.response.LikeResponseDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MainApiService {
    @WithAuthorization
    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @WithAuthorization
    @GET("promotions")
    suspend fun getPromotions(): List<PromotionDto>

    @WithAuthorization
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @WithAuthorization
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Long): ProductDto

    @WithAuthorization
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductDto>

    @WithAuthorization
    @POST("favorites/{productId}")
    suspend fun addToFavorites(@Path("productId") productId: Long): LikeResponseDto

    @WithAuthorization
    @DELETE("favorites/{productId}")
    suspend fun removeFromFavorites(@Path("productId") productId: Long): LikeResponseDto
}