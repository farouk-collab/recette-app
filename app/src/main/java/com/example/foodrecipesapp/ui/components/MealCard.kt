package com.example.foodrecipesapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodrecipesapp.domain.model.Meal
import com.example.foodrecipesapp.ui.theme.FoodCardBackground
import com.example.foodrecipesapp.ui.theme.FoodTextPrimary
import com.example.foodrecipesapp.ui.theme.FoodTextSecondary

@Composable
fun MealCard(
    meal: Meal,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = FoodCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = meal.imageUrl,
                contentDescription = meal.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            )

            Text(
                text = meal.title,
                color = FoodTextPrimary,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp, end = 12.dp)
            )

            Text(
                text = "Catégorie : ${meal.category.ifBlank { "Non précisée" }}",
                color = FoodTextSecondary,
                modifier = Modifier.padding(start = 12.dp, top = 6.dp, end = 12.dp)
            )

            Text(
                text = "Origine : ${meal.area.ifBlank { "Non précisée" }}",
                color = FoodTextSecondary,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}