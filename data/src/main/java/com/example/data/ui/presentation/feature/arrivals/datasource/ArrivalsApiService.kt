package com.example.data.ui.presentation.feature.arrivals.datasource

import com.example.data.ui.presentation.feature.arrivals.dto.PromotionDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.GET

interface ArrivalsApiService {
    @WithAuthorization
    @GET("promotions")
    suspend fun getPromotions(): List<PromotionDto>
}