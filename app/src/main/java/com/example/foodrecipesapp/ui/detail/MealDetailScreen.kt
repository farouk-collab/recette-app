package com.example.foodrecipesapp.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.foodrecipesapp.domain.model.Meal
import com.example.foodrecipesapp.ui.components.DecorativeBackground
import com.example.foodrecipesapp.ui.components.ErrorContent
import com.example.foodrecipesapp.ui.components.FoodTopBar
import com.example.foodrecipesapp.ui.theme.FoodBlush
import com.example.foodrecipesapp.ui.theme.FoodBorder
import com.example.foodrecipesapp.ui.theme.FoodCardBackground
import com.example.foodrecipesapp.ui.theme.FoodOrange
import com.example.foodrecipesapp.ui.theme.FoodSurface
import com.example.foodrecipesapp.ui.theme.FoodTextPrimary
import com.example.foodrecipesapp.ui.theme.FoodTextSecondary

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    viewModel: MealDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = onBack)

    LaunchedEffect(mealId) {
        viewModel.loadMealDetail(mealId)
    }

    when {
        uiState.isLoading -> {
            DecorativeBackground {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FoodTopBar(showTitle = false, onBackClick = onBack)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Chargement du detail...",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }

        uiState.error != null -> {
            DecorativeBackground {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FoodTopBar(showTitle = false, onBackClick = onBack)

                    ErrorContent(
                        message = uiState.error ?: "Erreur inconnue",
                        onRetry = { viewModel.loadMealDetail(mealId) }
                    )
                }
            }
        }

        uiState.meal != null -> {
            val meal = uiState.meal!!

            DecorativeBackground {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FoodTopBar(showTitle = false, onBackClick = onBack)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        HeroSection(meal = meal)

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            color = FoodCardBackground,
                            shape = RoundedCornerShape(28.dp),
                            border = BorderStroke(1.dp, FoodBorder.copy(alpha = 0.7f))
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = meal.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = FoodTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Une assiette qui donne envie de cuisiner tout de suite.",
                                    color = FoodTextSecondary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )

                                FlowRow(
                                    modifier = Modifier.padding(top = 18.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    InfoPill(
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Default.RestaurantMenu,
                                                contentDescription = null,
                                                tint = FoodOrange
                                            )
                                        },
                                        text = "${meal.ingredients.count { it.isNotBlank() }} ingredients"
                                    )

                                    InfoPill(
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = FoodOrange
                                            )
                                        },
                                        text = estimatePrepLabel(meal)
                                    )

                                    InfoPill(
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Default.SoupKitchen,
                                                contentDescription = null,
                                                tint = FoodOrange
                                            )
                                        },
                                        text = meal.area.ifBlank { "Cuisine du monde" }
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 18.dp),
                                    color = FoodBorder
                                )

                                DetailSectionTitle(
                                    title = "Ingredients",
                                    subtitle = "Tout ce qu'il faut pour reussir la recette"
                                )

                                Column(
                                    modifier = Modifier.padding(top = 14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    meal.ingredients
                                        .filter { it.isNotBlank() }
                                        .forEach { ingredient ->
                                            IngredientItem(ingredient = ingredient)
                                        }
                                }

                                if (meal.instructions.isNotBlank()) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 22.dp),
                                        color = FoodBorder
                                    )

                                    DetailSectionTitle(
                                        title = "Preparation",
                                        subtitle = "Des etapes simples pour bien cuisiner"
                                    )

                                    Column(
                                        modifier = Modifier.padding(top = 14.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        meal.instructions
                                            .splitInstructions()
                                            .forEachIndexed { index, step ->
                                                PreparationStep(
                                                    index = index + 1,
                                                    step = step
                                                )
                                            }
                                    }
                                }

                                Button(
                                    onClick = onBack,
                                    colors = ButtonDefaults.buttonColors(containerColor = FoodOrange),
                                    modifier = Modifier.padding(top = 24.dp)
                                ) {
                                    Text("Retour")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroSection(meal: Meal) {
    Box {
        AsyncImage(
            model = meal.imageUrl,
            contentDescription = meal.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(32.dp))
        )

        Surface(
            color = FoodSurface.copy(alpha = 0.92f),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = meal.category.ifBlank { "Categorie du chef" },
                    color = FoodOrange,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = meal.area.ifBlank { "Cuisine du monde" },
                    color = FoodTextSecondary
                )
            }
        }
    }
}

@Composable
private fun DetailSectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            color = FoodTextPrimary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtitle,
            color = FoodTextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun IngredientItem(ingredient: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(FoodSurface.copy(alpha = 0.75f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(FoodOrange)
        )

        Text(
            text = ingredient,
            color = FoodTextPrimary,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun PreparationStep(
    index: Int,
    step: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(FoodBlush.copy(alpha = 0.55f))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(FoodOrange),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = index.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = step,
            color = FoodTextPrimary,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun InfoPill(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(FoodSurface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Text(
            text = text,
            color = FoodTextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private fun String.splitInstructions(): List<String> {
    val cleaned = replace("\r", "\n")
    val splitByLines = cleaned
        .split("\n")
        .map { it.trim() }
        .filter { it.isNotBlank() }

    if (splitByLines.size > 1) {
        return splitByLines
    }

    return cleaned
        .split(Regex("(?<=[.!?])\\s+"))
        .map { it.trim() }
        .filter { it.isNotBlank() }
}

private fun estimatePrepLabel(meal: Meal): String {
    val steps = meal.instructions.splitInstructions().size.coerceAtLeast(1)
    val estimatedMinutes = (steps * 7).coerceAtLeast(15)
    return "~ $estimatedMinutes min"
}
