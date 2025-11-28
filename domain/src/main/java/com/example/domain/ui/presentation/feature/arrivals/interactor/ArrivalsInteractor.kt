package com.example.domain.ui.presentation.feature.arrivals.interactor

import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.domain.ui.presentation.feature.arrivals.repository.ArrivalsRepository

class ArrivalsInteractor(
    private val arrivalsRepository: ArrivalsRepository
) {

    suspend fun loadPromotions(): Result<List<Promotion>> {
        return arrivalsRepository.getPromotions()
    }

}