package com.example.foodrecipesapp

import android.app.Application
import com.example.foodrecipesapp.data.local.AppDatabase

class FoodApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
}