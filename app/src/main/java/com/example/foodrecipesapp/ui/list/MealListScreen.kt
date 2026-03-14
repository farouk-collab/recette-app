package com.example.foodrecipesapp.ui.list

import com.example.foodrecipesapp.ui.components.SearchBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodrecipesapp.ui.components.CategoryChips
import com.example.foodrecipesapp.ui.components.FoodTopBar
import com.example.foodrecipesapp.ui.components.MealCard
import com.example.foodrecipesapp.ui.theme.FoodOrange
import com.example.foodrecipesapp.ui.theme.FoodScreenBackground

@Composable
fun MealListScreen(
    onMealClick: (String) -> Unit,
    viewModel: MealListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading && uiState.meals.isEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FoodScreenBackground)
            ) {
                FoodTopBar()

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

        uiState.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FoodScreenBackground)
            ) {
                FoodTopBar()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error ?: "Erreur inconnue",
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = { viewModel.loadMeals() },
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FoodOrange
                        )
                    ) {
                        Text("Réessayer")
                    }
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FoodScreenBackground)
            ) {
                FoodTopBar()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SearchBar(
                            value = uiState.searchQuery,
                            onValueChange = { newValue ->
                                viewModel.onSearchQueryChange(newValue)
                            }
                        )
                    }

                    item {
                        Button(
                            onClick = { viewModel.loadMeals() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FoodOrange
                            )
                        ) {
                            Text("Rechercher")
                        }
                    }

                    if (uiState.categories.isNotEmpty()) {
                        item {
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

                    items(uiState.meals) { meal ->
                        MealCard(
                            meal = meal,
                            onClick = { onMealClick(meal.id) }
                        )
                    }
                }
            }
        }
    }
}