package com.example.foodrecipesapp.data.local

import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Meal

fun MealEntity.toMeal(): Meal {
    return Meal(
        id = id,
        title = title,
        category = category,
        area = area,
        imageUrl = imageUrl,
        instructions = instructions,
        ingredients = if (ingredientsText.isBlank()) {
            emptyList()
        } else {
            ingredientsText.split("|").map { it.trim() }
        }
    )
}

fun Meal.toEntity(): MealEntity {
    return MealEntity(
        id = id,
        title = title,
        category = category,
        area = area,
        imageUrl = imageUrl,
        instructions = instructions,
        ingredientsText = ingredients.joinToString("|")
    )
}

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        thumbnailUrl = thumbnailUrl
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        thumbnailUrl = thumbnailUrl
    )
}