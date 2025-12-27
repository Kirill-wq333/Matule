package com.example.domain.ui.presentation.feature.main.model

import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.domain.ui.presentation.feature.popular.model.Product

data class HomeContent(
    val popularProducts: List<Product>,
    val promotions: List<Promotion>
)
