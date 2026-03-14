package com.example.foodrecipesapp.data.remote

import com.example.foodrecipesapp.data.remote.dto.CategoriesResponse
import com.example.foodrecipesapp.data.remote.dto.MealDetailResponse
import com.example.foodrecipesapp.data.remote.dto.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealsResponse

    @GET("lookup.php")
    suspend fun getMealDetail(
        @Query("i") id: String
    ): MealDetailResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealsResponse
}