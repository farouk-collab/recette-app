package com.example.foodrecipesapp.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodrecipesapp.ui.components.CategoryChips
import com.example.foodrecipesapp.ui.components.CuisineChips
import com.example.foodrecipesapp.ui.components.DecorativeBackground
import com.example.foodrecipesapp.ui.components.ErrorContent
import com.example.foodrecipesapp.ui.components.FoodTopBar
import com.example.foodrecipesapp.ui.components.MealCard
import com.example.foodrecipesapp.ui.components.SearchBar
import com.example.foodrecipesapp.ui.theme.FoodOrange
import com.example.foodrecipesapp.ui.theme.FoodTextPrimary
import com.example.foodrecipesapp.ui.theme.FoodTextSecondary

@Composable
fun MealListScreen(
    onMealClick: (String) -> Unit,
    viewModel: MealListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading && uiState.visibleMeals.isEmpty() -> {
            DecorativeBackground {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FoodTopBar(
                        subtitle = "Des idees gourmandes a chaque ouverture"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Chargement des recettes...",
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
                    FoodTopBar(
                        subtitle = "Des idees gourmandes a chaque ouverture"
                    )

                    ErrorContent(
                        message = uiState.error ?: "Erreur inconnue",
                        onRetry = { viewModel.loadMeals() }
                    )
                }
            }
        }

        else -> {
            DecorativeBackground {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FoodTopBar(
                        subtitle = "Des idees gourmandes a chaque ouverture"
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Choisis ton envie du moment",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = FoodTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Explore des plats colores, rapides ou genereux selon ton humeur.",
                                    color = FoodTextSecondary,
                                    modifier = Modifier.padding(top = 6.dp)
                                )

                                Text(
                                    text = "Exemples : beef ou boeuf, chicken ou poulet",
                                    color = FoodTextSecondary,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }

                        item {
                            SearchBar(
                                value = uiState.searchQuery,
                                onValueChange = { newValue ->
                                    viewModel.onSearchQueryChange(newValue)
                                }
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { viewModel.loadMeals() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FoodOrange
                                    )
                                ) {
                                    Text("Rechercher")
                                }

                                Text(
                                    text = "${uiState.visibleMeals.size} recettes",
                                    color = FoodTextSecondary
                                )
                            }
                        }

                        if (uiState.categories.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Categories",
                                    color = FoodTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )

                                CategoryChips(
                                    categories = uiState.categories,
                                    selectedCategory = uiState.selectedCategory,
                                    onCategoryClick = { category ->
                                        viewModel.onCategorySelected(category)
                                    },
                                    onClearSelection = {
                                        viewModel.clearCategorySelection()
                                    }
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Origines et cuisines",
                                color = FoodTextPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )

                            CuisineChips(
                                cuisines = MealListViewModel.cuisineGroups,
                                selectedCuisine = uiState.selectedCuisine,
                                onCuisineClick = { cuisine ->
                                    viewModel.onCuisineSelected(cuisine)
                                },
                                onClearSelection = {
                                    viewModel.clearCuisineSelection()
                                }
                            )
                        }

                        items(
                            items = uiState.visibleMeals,
                            key = { meal -> meal.id }
                        ) { meal ->
                            MealCard(
                                meal = meal,
                                onClick = { onMealClick(meal.id) }
                            )
                        }

                        if (uiState.canLoadMore) {
                            item {
                                LaunchedEffect(uiState.currentPage, uiState.visibleMeals.size) {
                                    viewModel.loadNextPage()
                                }

                                CircularProgressIndicator(
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
