package com.example.domain.ui.presentation.feature.arrivals.repository

import com.example.domain.ui.presentation.feature.arrivals.model.Promotion

interface ArrivalsRepository {
    suspend fun getPromotions(): Result<List<Promotion>>
}