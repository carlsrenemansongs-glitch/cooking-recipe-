package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.GroceryEntity
import com.example.data.local.PantryEntity
import com.example.data.model.Recipe
import com.example.data.repository.BudgetCookRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class BudgetCookViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = BudgetCookRepository(db.recipeDao(), db.pantryDao(), db.groceryDao())

    private val _mealFilter = MutableStateFlow("All") // "All", "Lunch", "Dinner"
    val mealFilter: StateFlow<String> = _mealFilter

    private val _maxBudgetFilter = MutableStateFlow(3.00f) // Max $ per serving
    val maxBudgetFilter: StateFlow<Float> = _maxBudgetFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedTag = MutableStateFlow("All") // "All", "Quick", "High Protein", "Vegetarian", "Under $1.50"
    val selectedTag: StateFlow<String> = _selectedTag

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    val recipes: StateFlow<List<Recipe>> = combine(
        repository.allRecipes,
        _mealFilter,
        _maxBudgetFilter,
        _searchQuery,
        _selectedTag
    ) { all, meal, maxCost, query, tag ->
        all.filter { recipe ->
            // Meal Filter
            val matchesMeal = when (meal) {
                "Lunch" -> recipe.category.contains("Lunch", ignoreCase = true)
                "Dinner" -> recipe.category.contains("Dinner", ignoreCase = true)
                else -> true
            }

            // Budget Filter
            val matchesBudget = recipe.estimatedCost <= maxCost

            // Search Query
            val matchesSearch = query.isBlank() ||
                    recipe.title.contains(query, ignoreCase = true) ||
                    recipe.description.contains(query, ignoreCase = true) ||
                    recipe.ingredients.any { it.contains(query, ignoreCase = true) }

            // Tag Filter
            val matchesTag = tag == "All" || recipe.tags.any { it.equals(tag, ignoreCase = true) }

            matchesMeal && matchesBudget && matchesSearch && matchesTag
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteRecipes: StateFlow<List<Recipe>> = repository.favoriteRecipes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val pantryItems: StateFlow<List<PantryEntity>> = repository.pantryItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val groceryItems: StateFlow<List<GroceryEntity>> = repository.groceryItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setMealFilter(meal: String) {
        _mealFilter.value = meal
    }

    fun setMaxBudgetFilter(maxBudget: Float) {
        _maxBudgetFilter.value = maxBudget
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedTag(tag: String) {
        _selectedTag.value = tag
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe.id, recipe.isFavorite)
            val msg = if (!recipe.isFavorite) "Saved ${recipe.title} to Favorites!" else "Removed from Favorites"
            _snackbarEvent.emit(msg)
        }
    }

    fun updatePantryItem(id: Int, isInPantry: Boolean) {
        viewModelScope.launch {
            repository.updatePantryItem(id, isInPantry)
        }
    }

    fun addIngredientsToGrocery(recipe: Recipe) {
        viewModelScope.launch {
            repository.addRecipeIngredientsToGroceryList(recipe)
            _snackbarEvent.emit("Added ingredients for '${recipe.title}' to Grocery List!")
        }
    }

    fun addCustomGroceryItem(name: String, cost: Double) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addGroceryItem(name.trim(), cost)
            _snackbarEvent.emit("Added '$name' to Grocery List")
        }
    }

    fun toggleGroceryPurchased(id: Int, currentPurchased: Boolean) {
        viewModelScope.launch {
            repository.toggleGroceryPurchased(id, currentPurchased)
        }
    }

    fun deleteGroceryItem(item: GroceryEntity) {
        viewModelScope.launch {
            repository.deleteGroceryItem(item)
        }
    }

    fun clearPurchasedGrocery() {
        viewModelScope.launch {
            repository.clearPurchasedGroceryItems()
            _snackbarEvent.emit("Cleared completed grocery items")
        }
    }

    fun addCustomRecipe(
        title: String,
        category: String,
        costPerServing: Double,
        servings: Int,
        prepTime: Int,
        cookTime: Int,
        description: String,
        ingredientsText: String,
        instructionsText: String,
        budgetTipsText: String,
        cookingTipsText: String
    ) {
        viewModelScope.launch {
            val newRecipe = Recipe(
                id = UUID.randomUUID().toString(),
                title = title.ifBlank { "Budget Custom Meal" },
                category = category,
                estimatedCost = costPerServing,
                totalCost = costPerServing * servings,
                servings = servings,
                prepTimeMinutes = prepTime,
                cookTimeMinutes = cookTime,
                difficulty = "Easy",
                calories = 400,
                description = description.ifBlank { "A delicious home-cooked low-budget recipe." },
                ingredients = ingredientsText.lines().filter { it.isNotBlank() },
                instructions = instructionsText.lines().filter { it.isNotBlank() },
                budgetTips = budgetTipsText.lines().filter { it.isNotBlank() }.ifEmpty { listOf("Cook in batch to save extra.") },
                cookingTips = cookingTipsText.lines().filter { it.isNotBlank() }.ifEmpty { listOf("Season well with salt and garlic.") },
                tags = listOf("Custom", "Budget Smart"),
                drawableResName = "img_budget_meal_hero_1785616562687",
                isCustom = true
            )
            repository.addCustomRecipe(newRecipe)
            _snackbarEvent.emit("Created new low-budget recipe: $title")
        }
    }
}
