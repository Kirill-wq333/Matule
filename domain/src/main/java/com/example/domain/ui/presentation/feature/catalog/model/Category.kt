package com.example.domain.ui.presentation.feature.catalog.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val slug: String
)