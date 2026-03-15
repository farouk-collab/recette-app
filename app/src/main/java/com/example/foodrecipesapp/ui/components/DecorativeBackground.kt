package com.example.foodrecipesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.foodrecipesapp.ui.theme.FoodBlush
import com.example.foodrecipesapp.ui.theme.FoodCream
import com.example.foodrecipesapp.ui.theme.FoodOrangeSoft
import com.example.foodrecipesapp.ui.theme.FoodScreenBackground

@Composable
fun DecorativeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(FoodCream, FoodScreenBackground, Color(0xFFFFFDF9))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-40).dp, y = (-20).dp)
                .size(180.dp)
                .blur(16.dp)
                .background(
                    color = FoodBlush.copy(alpha = 0.75f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = 10.dp)
                .size(220.dp)
                .blur(20.dp)
                .background(
                    color = FoodOrangeSoft.copy(alpha = 0.28f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 60.dp)
                .size(200.dp)
                .blur(22.dp)
                .background(
                    color = FoodOrangeSoft.copy(alpha = 0.22f),
                    shape = CircleShape
                )
        )

        content()
    }
}
