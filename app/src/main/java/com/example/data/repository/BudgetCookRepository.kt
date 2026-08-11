package com.example.data.repository

import com.example.data.local.GroceryDao
import com.example.data.local.GroceryEntity
import com.example.data.local.PantryDao
import com.example.data.local.PantryEntity
import com.example.data.local.RecipeDao
import com.example.data.local.RecipeEntity
import com.example.data.local.SeedData
import com.example.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetCookRepository(
    private val recipeDao: RecipeDao,
    private val pantryDao: PantryDao,
    private val groceryDao: GroceryDao
) {
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes().map { entities ->
        entities.map { Recipe.fromEntity(it) }
    }

    val favoriteRecipes: Flow<List<Recipe>> = recipeDao.getFavoriteRecipes().map { entities ->
        entities.map { Recipe.fromEntity(it) }
    }

    val pantryItems: Flow<List<PantryEntity>> = pantryDao.getAllPantryItems()

    val groceryItems: Flow<List<GroceryEntity>> = groceryDao.getAllGroceryItems()

    fun getRecipeById(id: String): Flow<Recipe?> = recipeDao.getRecipeById(id).map { entity ->
        entity?.let { Recipe.fromEntity(it) }
    }

    suspend fun initializeSeedDataIfNeeded() {
        if (recipeDao.getRecipeCount() == 0) {
            recipeDao.insertRecipes(SeedData.defaultRecipes)
        }
        if (pantryDao.getPantryCount() == 0) {
            pantryDao.insertPantryItems(SeedData.defaultPantry)
        }
    }

    suspend fun toggleFavorite(recipeId: String, currentFavorite: Boolean) {
        recipeDao.updateFavoriteStatus(recipeId, !currentFavorite)
    }

    suspend fun updatePantryItem(id: Int, isInPantry: Boolean) {
        pantryDao.updatePantryStatus(id, isInPantry)
    }

    suspend fun addCustomRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe.toEntity())
    }

    suspend fun addRecipeIngredientsToGroceryList(recipe: Recipe) {
        val items = recipe.ingredients.map { ingredient ->
            GroceryEntity(
                name = ingredient,
                estimatedCost = (recipe.estimatedCost * 0.4).coerceAtLeast(0.50), // estimated individual ingredient portion cost
                isPurchased = false,
                recipeTitle = recipe.title
            )
        }
        groceryDao.insertGroceryItems(items)
    }

    suspend fun addGroceryItem(name: String, estimatedCost: Double) {
        groceryDao.insertGroceryItem(
            GroceryEntity(
                name = name,
                estimatedCost = estimatedCost,
                isPurchased = false,
                recipeTitle = "Custom"
            )
        )
    }

    suspend fun toggleGroceryPurchased(id: Int, currentPurchased: Boolean) {
        groceryDao.updateGroceryStatus(id, !currentPurchased)
    }

    suspend fun deleteGroceryItem(item: GroceryEntity) {
        groceryDao.deleteGroceryItem(item)
    }

    suspend fun clearPurchasedGroceryItems() {
        groceryDao.clearPurchased()
    }
}
