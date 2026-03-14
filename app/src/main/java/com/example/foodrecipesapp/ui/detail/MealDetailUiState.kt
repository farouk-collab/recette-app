package com.example.foodrecipesapp.ui.detail

import com.example.foodrecipesapp.domain.model.Meal

data class MealDetailUiState(
    val isLoading: Boolean = false,
    val meal: Meal? = null,
    val error: String? = null
)