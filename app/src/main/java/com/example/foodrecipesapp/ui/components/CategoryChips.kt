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
import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.ui.theme.FoodOrange

@Composable
fun CategoryChips(
    categories: List<Category>,
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit,
    onClearSelection: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = onClearSelection,
                label = { Text("Tous") },
                modifier = Modifier.padding(vertical = 2.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = FoodOrange,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = FoodOrange
                )
            )
        }

        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category.name,
                onClick = { onCategoryClick(category.name) },
                label = { Text(category.name) },
                modifier = Modifier.padding(vertical = 2.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = FoodOrange,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = FoodOrange
                )
            )
        }
    }
}