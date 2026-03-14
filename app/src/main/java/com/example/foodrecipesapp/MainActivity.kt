package com.example.foodrecipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.foodrecipesapp.navigation.AppScreen
import com.example.foodrecipesapp.ui.detail.MealDetailScreen
import com.example.foodrecipesapp.ui.list.MealListScreen
import com.example.foodrecipesapp.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }

            when (val screen = currentScreen) {
                AppScreen.Splash -> {
                    SplashScreen(
                        onFinished = {
                            currentScreen = AppScreen.List
                        }
                    )
                }

                AppScreen.List -> {
                    MealListScreen(
                        onMealClick = { mealId ->
                            currentScreen = AppScreen.Detail(mealId)
                        }
                    )
                }

                is AppScreen.Detail -> {
                    MealDetailScreen(
                        mealId = screen.mealId,
                        onBack = {
                            currentScreen = AppScreen.List
                        }
                    )
                }
            }
        }
    }
}