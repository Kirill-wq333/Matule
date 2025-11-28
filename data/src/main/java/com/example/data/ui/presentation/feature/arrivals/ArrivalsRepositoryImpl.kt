package com.example.data.ui.presentation.feature.arrivals

import com.example.data.ui.presentation.feature.arrivals.datasource.ArrivalsApiService
import com.example.data.ui.presentation.feature.arrivals.dto.PromotionDto.Companion.toPromotion
import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.domain.ui.presentation.feature.arrivals.repository.ArrivalsRepository
import javax.inject.Inject

class ArrivalsRepositoryImpl @Inject constructor(
    private val apiService: ArrivalsApiService
): ArrivalsRepository {

    override suspend fun getPromotions(): Result<List<Promotion>> {
        return try {
            val response = apiService.getPromotions()
            Result.success(response.map { it.toPromotion() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}