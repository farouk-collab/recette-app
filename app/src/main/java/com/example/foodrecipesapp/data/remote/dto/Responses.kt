package com.example.foodrecipesapp.data.remote.dto

data class MealsResponse(
    val meals: List<MealDto>?
)

data class MealDetailResponse(
    val meals: List<MealDto>?
)

data class CategoriesResponse(
    val categories: List<CategoryDto>
)