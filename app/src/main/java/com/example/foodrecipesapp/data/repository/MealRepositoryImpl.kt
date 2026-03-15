package com.example.foodrecipesapp.data.repository

import com.example.foodrecipesapp.data.local.CategoryDao
import com.example.foodrecipesapp.data.local.MealDao
import com.example.foodrecipesapp.data.local.SyncInfoDao
import com.example.foodrecipesapp.data.local.SyncInfoEntity
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
    private val categoryDao: CategoryDao,
    private val syncInfoDao: SyncInfoDao
) : MealRepository {

    companion object {
        private const val STALE_AFTER_MS = 6 * 60 * 60 * 1000L
        private const val DEFAULT_PRELOAD_QUERY = "chicken"
    }

    override suspend fun searchMeals(query: String): List<Meal> {
        val syncKey = "search:${query.lowercase()}"

        return try {
            val cachedMeals = mealDao.searchMeals(query).map { it.toMeal() }
            if (cachedMeals.isNotEmpty() && !shouldRefresh(syncKey)) {
                return cachedMeals
            }

            val remoteMeals = api.searchMeals(query).meals?.map { it.toMeal() } ?: emptyList()
            if (remoteMeals.isNotEmpty()) {
                mealDao.insertMeals(remoteMeals.map { it.toEntity() })
            }
            markSynced(syncKey)
            remoteMeals.ifEmpty { cachedMeals }
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
        val syncKey = "categories"

        return try {
            val cachedCategories = categoryDao.getAllCategories().map { it.toCategory() }
            if (cachedCategories.isNotEmpty() && !shouldRefresh(syncKey)) {
                return cachedCategories
            }

            val remoteCategories = api.getCategories().categories.map { it.toCategory() }
            categoryDao.clearCategories()
            categoryDao.insertCategories(remoteCategories.map { it.toEntity() })
            markSynced(syncKey)
            remoteCategories
        } catch (_: Exception) {
            categoryDao.getAllCategories().map { it.toCategory() }
        }
    }

    override suspend fun getMealsByCategory(category: String): List<Meal> {
        val syncKey = "category:${category.lowercase()}"

        return try {
            val cachedMeals = mealDao.getMealsByCategory(category).map { it.toMeal() }
            if (cachedMeals.isNotEmpty() && !shouldRefresh(syncKey)) {
                return cachedMeals
            }

            val remoteMeals = api.getMealsByCategory(category).meals?.map { it.toMeal() } ?: emptyList()
            if (remoteMeals.isNotEmpty()) {
                mealDao.insertMeals(remoteMeals.map { it.toEntity() })
            }
            markSynced(syncKey)
            remoteMeals.ifEmpty { cachedMeals }
        } catch (_: Exception) {
            mealDao.getMealsByCategory(category).map { it.toMeal() }
        }
    }

    override suspend fun getMealsByArea(area: String): List<Meal> {
        val syncKey = "area:${area.lowercase()}"

        return try {
            val cachedMeals = mealDao.getMealsByArea(area).map { it.toMeal() }
            if (cachedMeals.isNotEmpty() && !shouldRefresh(syncKey)) {
                return cachedMeals
            }

            val remoteMeals = api.getMealsByArea(area).meals?.map { it.toMeal() } ?: emptyList()
            if (remoteMeals.isNotEmpty()) {
                mealDao.insertMeals(remoteMeals.map { it.toEntity() })
            }
            markSynced(syncKey)
            remoteMeals.ifEmpty { cachedMeals }
        } catch (_: Exception) {
            mealDao.getMealsByArea(area).map { it.toMeal() }
        }
    }

    override suspend fun preloadInitialData() {
        try {
            getCategories()
            searchMeals(DEFAULT_PRELOAD_QUERY)
        } catch (_: Exception) {
        }
    }

    override suspend fun refreshCategories() {
        val remoteCategories = api.getCategories().categories.map { it.toCategory() }
        categoryDao.clearCategories()
        categoryDao.insertCategories(remoteCategories.map { it.toEntity() })
        markSynced("categories")
    }

    private suspend fun shouldRefresh(key: String): Boolean {
        val syncInfo = syncInfoDao.getSyncInfo(key) ?: return true
        return System.currentTimeMillis() - syncInfo.lastSyncTime > STALE_AFTER_MS
    }

    private suspend fun markSynced(key: String) {
        syncInfoDao.insertSyncInfo(
            SyncInfoEntity(
                key = key,
                lastSyncTime = System.currentTimeMillis()
            )
        )
    }
}
