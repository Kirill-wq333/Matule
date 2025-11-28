package com.example.data.ui.presentation.feature.arrivals.dto

import com.example.domain.ui.presentation.feature.arrivals.model.Promotion

data class PromotionDto(
    val id: Long,
    val image: String,
    val validUntil: String,
    val isActive: Boolean,
    val createdAt: String
) {
    companion object {
        fun PromotionDto.toPromotion(): Promotion =
            Promotion(
                id = id,
                image = image,
                validUntil = validUntil,
                isActive = isActive,
                createdAt = createdAt
            )
    }
}