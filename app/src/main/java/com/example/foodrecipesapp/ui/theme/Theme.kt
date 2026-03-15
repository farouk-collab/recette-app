package com.example.foodrecipesapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = FoodOrange,
    onPrimary = FoodCream,
    secondary = FoodOrangeDark,
    onSecondary = FoodCream,
    tertiary = FoodOrangeSoft,
    background = FoodScreenBackground,
    onBackground = FoodTextPrimary,
    surface = FoodCardBackground,
    onSurface = FoodTextPrimary,
    surfaceVariant = FoodSurface,
    outline = FoodBorder
)

@Composable
fun FoodRecipesAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
