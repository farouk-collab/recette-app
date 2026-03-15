package com.example.foodrecipesapp.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.foodrecipesapp.ui.components.DecorativeBackground
import com.example.foodrecipesapp.ui.components.ErrorContent
import com.example.foodrecipesapp.ui.components.FoodTopBar
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
                        Box {
                            AsyncImage(
                                model = meal.imageUrl,
                                contentDescription = meal.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
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

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 18.dp),
                                    color = FoodBorder
                                )

                                Text(
                                    text = "Ingredients",
                                    color = FoodTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = meal.ingredients.joinToString("\n"),
                                    color = FoodTextSecondary,
                                    modifier = Modifier.padding(top = 12.dp)
                                )

                                if (meal.instructions.isNotBlank()) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 18.dp),
                                        color = FoodBorder
                                    )

                                    Text(
                                        text = "Preparation",
                                        color = FoodTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = meal.instructions,
                                        color = FoodTextSecondary,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )
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
