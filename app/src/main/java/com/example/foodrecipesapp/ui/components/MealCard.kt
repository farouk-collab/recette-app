package com.example.foodrecipesapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodrecipesapp.domain.model.Meal
import com.example.foodrecipesapp.ui.theme.FoodBlush
import com.example.foodrecipesapp.ui.theme.FoodBorder
import com.example.foodrecipesapp.ui.theme.FoodCardBackground
import com.example.foodrecipesapp.ui.theme.FoodOrange
import com.example.foodrecipesapp.ui.theme.FoodOrangeSoft
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
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = FoodCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, FoodBorder.copy(alpha = 0.7f))
    ) {
        Column {
            Box {
                AsyncImage(
                    model = meal.imageUrl,
                    contentDescription = meal.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x33000000),
                                    Color(0x77000000)
                                )
                            )
                        )
                )

                Text(
                    text = meal.area.ifBlank { "Cuisine du monde" },
                    color = FoodTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(14.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(FoodBlush.copy(alpha = 0.92f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = meal.title,
                    color = FoodTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = meal.category.ifBlank { "Categorie du jour" },
                    color = FoodTextSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fraichement inspiree",
                        color = FoodOrangeSoft,
                        fontWeight = FontWeight.Medium
                    )

                    TextButton(onClick = onClick) {
                        Text(
                            text = "Voir",
                            color = FoodOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
