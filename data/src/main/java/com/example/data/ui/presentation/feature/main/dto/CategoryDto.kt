package com.example.data.ui.presentation.feature.main.dto

import com.example.domain.ui.presentation.feature.main.model.Category

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