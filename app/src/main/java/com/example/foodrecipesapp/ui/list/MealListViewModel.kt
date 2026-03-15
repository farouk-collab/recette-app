package com.example.foodrecipesapp.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.FoodApplication
import com.example.foodrecipesapp.data.remote.RetrofitInstance
import com.example.foodrecipesapp.data.repository.MealRepositoryImpl
import com.example.foodrecipesapp.domain.model.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer

class MealListViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val cuisineGroups: List<String> = listOf(
            "Francais",
            "Europeen",
            "Africain",
            "Americain",
            "Asiatique",
            "Indien"
        )

        private val cuisineAreaMap: Map<String, List<String>> = mapOf(
            "Francais" to listOf("French"),
            "Europeen" to listOf("French", "Italian", "British", "Croatian", "Dutch", "Greek", "Irish", "Polish", "Portuguese", "Russian", "Spanish", "Ukrainian"),
            "Africain" to listOf("Moroccan", "Tunisian", "Egyptian"),
            "Americain" to listOf("American", "Canadian", "Jamaican", "Mexican"),
            "Asiatique" to listOf("Chinese", "Japanese", "Korean", "Malaysian", "Thai", "Vietnamese", "Filipino"),
            "Indien" to listOf("Indian")
        )

        private val searchSynonyms: Map<String, List<String>> = mapOf(
            "boeuf" to listOf("beef"),
            "boeuf hache" to listOf("beef"),
            "viande" to listOf("beef", "lamb", "pork"),
            "poulet" to listOf("chicken"),
            "porc" to listOf("pork"),
            "agneau" to listOf("lamb"),
            "poisson" to listOf("fish", "salmon", "tuna"),
            "riz" to listOf("rice"),
            "dessert" to listOf("dessert"),
            "gateau" to listOf("cake", "dessert"),
            "crevette" to listOf("prawn", "shrimp"),
            "fruits de mer" to listOf("seafood")
        )
    }

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
            selectedCategory = null,
            selectedCuisine = null
        )
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            selectedCuisine = null
        )
        loadMealsByCategory(category)
    }

    fun onCuisineSelected(cuisine: String) {
        _uiState.value = _uiState.value.copy(
            selectedCuisine = cuisine,
            selectedCategory = null
        )
        loadMealsByCuisine(cuisine)
    }

    fun clearCategorySelection() {
        _uiState.value = _uiState.value.copy(selectedCategory = null)
        loadMeals()
    }

    fun clearCuisineSelection() {
        _uiState.value = _uiState.value.copy(selectedCuisine = null)
        loadMeals()
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()
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
                selectedCategory = null,
                selectedCuisine = null
            )

            try {
                val rawQuery = _uiState.value.searchQuery.ifBlank { "chicken" }
                val meals = searchMealsWithAliases(rawQuery)
                setMeals(meals)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = emptyList(),
                    visibleMeals = emptyList(),
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
                setMeals(meals)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = emptyList(),
                    visibleMeals = emptyList(),
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    fun loadMealsByCuisine(cuisine: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val areas = cuisineAreaMap[cuisine].orEmpty()
                val meals = areas
                    .flatMap { repository.getMealsByArea(it) }
                    .distinctBy { it.id }
                    .sortedBy { it.title }

                setMeals(meals)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    meals = emptyList(),
                    visibleMeals = emptyList(),
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || !state.canLoadMore) {
            return
        }

        val nextPage = state.currentPage + 1
        val nextVisibleMeals = state.meals.take(nextPage * state.pageSize)

        _uiState.value = state.copy(
            visibleMeals = nextVisibleMeals,
            currentPage = nextPage,
            canLoadMore = nextVisibleMeals.size < state.meals.size
        )
    }

    private suspend fun searchMealsWithAliases(rawQuery: String): List<Meal> {
        val normalizedQuery = normalize(rawQuery)
        val candidateQueries = buildList {
            add(rawQuery.trim())
            add(normalizedQuery)
            addAll(searchSynonyms[normalizedQuery].orEmpty())
        }
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()

        return candidateQueries
            .flatMap { repository.searchMeals(it) }
            .distinctBy { it.id }
            .sortedBy { it.title }
    }

    private fun setMeals(meals: List<Meal>) {
        val pageSize = _uiState.value.pageSize
        val visibleMeals = meals.take(pageSize)

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            meals = meals,
            visibleMeals = visibleMeals,
            error = null,
            currentPage = 1,
            canLoadMore = visibleMeals.size < meals.size
        )
    }

    private fun normalize(value: String): String {
        val normalized = Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD)
        return normalized.replace("\\p{M}+".toRegex(), "")
    }
}
