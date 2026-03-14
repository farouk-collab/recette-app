package com.example.foodrecipesapp.navigation

sealed class AppScreen {
    data object Splash : AppScreen()
    data object List : AppScreen()
    data class Detail(val mealId: String) : AppScreen()
}