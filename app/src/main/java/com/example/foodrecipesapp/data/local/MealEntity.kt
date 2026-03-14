package com.example.foodrecipesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val area: String,
    val imageUrl: String,
    val instructions: String,
    val ingredientsText: String
)