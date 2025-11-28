package com.example.data.ui.presentation.feature.favorite.datasource

import com.example.data.ui.presentation.feature.main.dto.response.LikeResponseDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApiService {

    @WithAuthorization
    @POST("favorites/{productId}")
    suspend fun addToFavorites(@Path("productId") productId: Long): LikeResponseDto

    @WithAuthorization
    @DELETE("favorites/{productId}")
    suspend fun removeFromFavorites(@Path("productId") productId: Long): LikeResponseDto

}