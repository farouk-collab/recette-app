package com.example.foodrecipesapp.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.FoodApplication
import com.example.foodrecipesapp.data.remote.RetrofitInstance
import com.example.foodrecipesapp.data.repository.MealRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val database = (application as FoodApplication).database

    private val repository = MealRepositoryImpl(
        api = RetrofitInstance.api,
        mealDao = database.mealDao(),
        categoryDao = database.categoryDao(),
        syncInfoDao = database.syncInfoDao()
    )

    private val _uiState = MutableStateFlow(MealDetailUiState())
    val uiState: StateFlow<MealDetailUiState> = _uiState.asStateFlow()

    fun loadMealDetail(mealId: String) {
        viewModelScope.launch {
            _uiState.value = MealDetailUiState(isLoading = true)

            try {
                val meal = repository.getMealDetail(mealId)
                _uiState.value = MealDetailUiState(
                    isLoading = false,
                    meal = meal,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = MealDetailUiState(
                    isLoading = false,
                    meal = null,
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }
}
