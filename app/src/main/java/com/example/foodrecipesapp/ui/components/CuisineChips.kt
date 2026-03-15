package com.example.foodrecipesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.foodrecipesapp.ui.theme.FoodCardBackground
import com.example.foodrecipesapp.ui.theme.FoodChipBackground
import com.example.foodrecipesapp.ui.theme.FoodOrange
import com.example.foodrecipesapp.ui.theme.FoodTextSecondary

@Composable
fun CuisineChips(
    cuisines: List<String>,
    selectedCuisine: String?,
    onCuisineClick: (String) -> Unit,
    onClearSelection: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCuisine == null,
                onClick = onClearSelection,
                label = { Text("Toutes") },
                modifier = Modifier.padding(vertical = 2.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = FoodOrange,
                    selectedLabelColor = Color.White,
                    containerColor = FoodChipBackground,
                    labelColor = FoodTextSecondary
                )
            )
        }

        items(cuisines) { cuisine ->
            FilterChip(
                selected = selectedCuisine == cuisine,
                onClick = { onCuisineClick(cuisine) },
                label = { Text(cuisine) },
                modifier = Modifier.padding(vertical = 2.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = FoodOrange,
                    selectedLabelColor = Color.White,
                    containerColor = FoodCardBackground,
                    labelColor = FoodTextSecondary
                )
            )
        }
    }
}
