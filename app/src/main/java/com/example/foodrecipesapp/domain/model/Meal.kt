package com.example.foodrecipesapp.domain.model

data class Meal(
    val id: String,
    val title: String,
    val category: String,
    val area: String,
    val imageUrl: String,
    val instructions: String,
    val ingredients: List<String>
)