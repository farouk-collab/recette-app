package com.example.foodrecipesapp.domain.repository

import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Meal

interface MealRepository {
    suspend fun searchMeals(query: String): List<Meal>
    suspend fun getMealDetail(id: String): Meal?
    suspend fun getCategories(): List<Category>
    suspend fun getMealsByCategory(category: String): List<Meal>
    suspend fun getMealsByArea(area: String): List<Meal>
    suspend fun refreshCategories()
}
