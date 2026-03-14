package com.example.foodrecipesapp.ui.detail

import com.example.foodrecipesapp.ui.components.FoodTopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    viewModel: MealDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mealId) {
        viewModel.loadMealDetail(mealId)
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Chargement du détail...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        uiState.error != null -> {
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
                    onClick = onBack,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Retour")
                }
            }
        }

        uiState.meal != null -> {
            val meal = uiState.meal!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color(0xFFEDEDED))
            ) {
                FoodTopBar(showTitle = false)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = meal.imageUrl,
                        contentDescription = meal.title,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = meal.title,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "Updated January 1st, 2023",
                            color = androidx.compose.ui.graphics.Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Text(
                            text = meal.ingredients.joinToString("\n"),
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        Button(
                            onClick = onBack,
                            modifier = Modifier.padding(top = 20.dp)
                        ) {
                            Text("Retour")
                        }
                    }
                }
            }
        }
    }
}
