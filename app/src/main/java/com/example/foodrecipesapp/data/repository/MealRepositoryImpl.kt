package com.example.foodrecipesapp.data.repository

import com.example.foodrecipesapp.data.local.CategoryDao
import com.example.foodrecipesapp.data.local.MealDao
import com.example.foodrecipesapp.data.local.toCategory
import com.example.foodrecipesapp.data.local.toEntity
import com.example.foodrecipesapp.data.local.toMeal
import com.example.foodrecipesapp.data.remote.MealApi
import com.example.foodrecipesapp.data.remote.dto.toCategory
import com.example.foodrecipesapp.data.remote.dto.toMeal
import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Meal
import com.example.foodrecipesapp.domain.repository.MealRepository

class MealRepositoryImpl(
    private val api: MealApi,
    private val mealDao: MealDao,
    private val categoryDao: CategoryDao
) : MealRepository {

    override suspend fun searchMeals(query: String): List<Meal> {
        return try {
            val remoteMeals = api.searchMeals(query).meals?.map { it.toMeal() } ?: emptyList()
            mealDao.insertMeals(remoteMeals.map { it.toEntity() })
            remoteMeals
        } catch (_: Exception) {
            mealDao.searchMeals(query).map { it.toMeal() }
        }
    }

    override suspend fun getMealDetail(id: String): Meal? {
        return try {
            val remoteMeal = api.getMealDetail(id).meals?.firstOrNull()?.toMeal()
            if (remoteMeal != null) {
                mealDao.insertMeal(remoteMeal.toEntity())
            }
            remoteMeal ?: mealDao.getMealById(id)?.toMeal()
        } catch (_: Exception) {
            mealDao.getMealById(id)?.toMeal()
        }
    }

    override suspend fun getCategories(): List<Category> {
        return try {
            val remoteCategories = api.getCategories().categories.map { it.toCategory() }
            categoryDao.clearCategories()
            categoryDao.insertCategories(remoteCategories.map { it.toEntity() })
            remoteCategories
        } catch (_: Exception) {
            categoryDao.getAllCategories().map { it.toCategory() }
        }
    }

    override suspend fun getMealsByCategory(category: String): List<Meal> {
        return try {
            val remoteMeals = api.getMealsByCategory(category).meals?.map { it.toMeal() } ?: emptyList()
            mealDao.insertMeals(remoteMeals.map { it.toEntity() })
            remoteMeals
        } catch (_: Exception) {
            mealDao.getMealsByCategory(category).map { it.toMeal() }
        }
    }

    override suspend fun refreshCategories() {
        val remoteCategories = api.getCategories().categories.map { it.toCategory() }
        categoryDao.clearCategories()
        categoryDao.insertCategories(remoteCategories.map { it.toEntity() })
    }
}