package com.example.foodrecipesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao{

    @Query("SELECT * FROM meals ORDER BY title ASC")
    suspend fun getAllMeals(): List<MealEntity>

    @Query("SELECT * FROM meals WHERE id = :id LIMIT 1")
    suspend fun getMealById(id: String): MealEntity?

    @Query("SELECT * FROM meals WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    suspend fun searchMeals(query: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE category = :category ORDER BY title ASC")
    suspend fun getMealsByCategory(category: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE area = :area ORDER BY title ASC")
    suspend fun getMealsByArea(area: String): List<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Query("DELETE FROM meals")
    suspend fun clearMeals()
}
