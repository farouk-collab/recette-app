package com.example.foodrecipesapp.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.FoodApplication
import com.example.foodrecipesapp.data.remote.RetrofitInstance
import com.example.foodrecipesapp.data.repository.MealRepositoryImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val database = (application as FoodApplication).database

    private val repository = MealRepositoryImpl(
        api = RetrofitInstance.api,
        mealDao = database.mealDao(),
        categoryDao = database.categoryDao(),
        syncInfoDao = database.syncInfoDao()
    )

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            awaitAll(
                async { repository.preloadInitialData() },
                async { delay(1200) }
            )
            _isReady.value = true
        }
    }
}
