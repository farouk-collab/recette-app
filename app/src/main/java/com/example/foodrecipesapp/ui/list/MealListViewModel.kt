package com.example.foodrecipesapp.ui.list

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

class MealListViewModel(application: Application) : AndroidViewModel(application) {

    private val database = (application as FoodApplication).database

    private val repository = MealRepositoryImpl(
        api = RetrofitInstance.api,
        mealDao = database.mealDao(),
        categoryDao = database.categoryDao()
    )

    private val _uiState = MutableStateFlow(MealListUiState())
    val uiState: StateFlow<MealListUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        loadMeals()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            selectedCategory = null
        )
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            searchQuery = category.lowercase()
        )
        loadMealsByCategory(category)
    }

    fun clearCategorySelection() {
        _uiState.value = _uiState.value.copy(selectedCategory = null)
        loadMeals()
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val allowedNames = listOf(
                    "Chicken",
                    "Beef",
                    "Soup",
                    "Dessert",
                    "Vegetarian",
                    "French"
                )

                val categories = repository.getCategories().filter { it.name in allowedNames }
                _uiState.value = _uiState.value.copy(categories = categories)
            } catch (_: Exception) {
            }
        }
    }

    fun loadMeals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                selectedCategory = null
            )

            try {
                val query = _uiState.value.searchQuery.ifBlank { "chicken" }
                val meals = repository.searchMeals(query)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = meals,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = emptyList(),
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    fun loadMealsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val meals = repository.getMealsByCategory(category)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = meals,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = emptyList(),
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }
}
