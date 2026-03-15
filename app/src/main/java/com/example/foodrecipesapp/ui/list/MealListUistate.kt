package com.example.foodrecipesapp.ui.list

import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Meal

data class MealListUiState(
    val isLoading: Boolean = false,
    val meals: List<Meal> = emptyList(),
    val visibleMeals: List<Meal> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: String? = null,
    val selectedCuisine: String? = null,
    val searchQuery: String = "chicken",
    val error: String? = null,
    val currentPage: Int = 1,
    val pageSize: Int = 30,
    val canLoadMore: Boolean = false
)
