package com.example.foodrecipesapp.data.remote.dto

import com.example.foodrecipesapp.domain.model.Category
data class CategoryDto(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

fun CategoryDto.toCategory(): Category {
    return Category(
        id = idCategory,
        name = strCategory,
        thumbnailUrl = strCategoryThumb
    )
}
