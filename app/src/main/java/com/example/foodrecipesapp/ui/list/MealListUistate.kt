package com.example.foodrecipesapp.ui.list

import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Meal

data class MealListUiState(
    val isLoading: Boolean = false,
    val meals: List<Meal> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "chicken",
    val error: String? = null
)