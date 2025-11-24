package com.example.data.ui.presentation.feature.main.dto.response

import com.example.data.ui.presentation.feature.main.dto.CategoryDto
import com.example.data.ui.presentation.feature.main.dto.CategoryDto.Companion.toCategory
import com.example.data.ui.presentation.feature.main.dto.ProductDto
import com.example.data.ui.presentation.feature.main.dto.ProductDto.Companion.toProductDto
import com.example.data.ui.presentation.feature.main.dto.PromotionDto
import com.example.data.ui.presentation.feature.main.dto.PromotionDto.Companion.toPromotion
import com.example.domain.ui.presentation.feature.main.model.HomeContent

data class MainResponseDto(
    val categories: List<CategoryDto>,
    val popularProducts: List<ProductDto>,
    val promotions: List<PromotionDto>
) {
    companion object {
        fun MainResponseDto.toHomeContent(): HomeContent =
            HomeContent(
                categories = categories.map { it.toCategory() },
                popularProducts = popularProducts.map { it.toProductDto() },
                promotions = promotions.map { it.toPromotion() }
            )
    }
}

data class LikeResponseDto(
    val success: Boolean,
    val message: String? = null
)