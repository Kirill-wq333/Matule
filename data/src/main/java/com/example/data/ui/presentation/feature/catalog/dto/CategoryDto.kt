package com.example.data.ui.presentation.feature.catalog.dto

import com.example.domain.ui.presentation.feature.catalog.model.Category

data class CategoryDto(
    val id: Long,
    val name: String,
    val slug: String
) {
    companion object {
        fun CategoryDto.toCategory(): Category =
            Category(
                id = id,
                name = name,
                slug = slug
            )
    }
}