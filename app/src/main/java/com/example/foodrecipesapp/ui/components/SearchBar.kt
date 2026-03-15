package com.example.foodrecipesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodrecipesapp.ui.theme.FoodBorder
import com.example.foodrecipesapp.ui.theme.FoodCardBackground
import com.example.foodrecipesapp.ui.theme.FoodOrange
import com.example.foodrecipesapp.ui.theme.FoodTextPrimary
import com.example.foodrecipesapp.ui.theme.FoodTextSecondary

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Chercher une recette") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Chercher"
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(22.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FoodCardBackground,
            unfocusedContainerColor = FoodCardBackground,
            focusedBorderColor = FoodOrange,
            unfocusedBorderColor = FoodBorder,
            focusedLeadingIconColor = FoodOrange,
            unfocusedLeadingIconColor = FoodTextSecondary,
            focusedTextColor = FoodTextPrimary,
            unfocusedTextColor = FoodTextPrimary,
            cursorColor = FoodOrange
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(FoodCardBackground, RoundedCornerShape(22.dp))
            .padding(horizontal = 8.dp)
    )
}
