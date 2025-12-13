package com.example.data.ui.presentation.feature.main.dto.response

//data class MainResponseDto(
//    val categories: List<CategoryDto>,
//    val popularProducts: List<ProductDto>,
//    val promotions: List<PromotionDto>
//)
//{
//    companion object {
//        fun MainResponseDto.toHomeContent(): HomeContent =
//            HomeContent(
//                categories = categories.map { it.toCategory() },
//                popularProducts = popularProducts.map { it.toProductDto() },
//                promotions = promotions.map { it.toPromotion() }
//            )
//    }
//}

data class LikeResponseDto(
    val success: Boolean,
    val message: String? = null
)