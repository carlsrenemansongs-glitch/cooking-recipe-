package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "Lunch", "Dinner", "Lunch & Dinner"
    val estimatedCost: Double, // Cost per serving e.g. 1.85
    val totalCost: Double, // Batch cost e.g. 5.55
    val servings: Int,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: String, // "Easy", "Medium"
    val calories: Int,
    val description: String,
    val ingredientsRaw: String, // Pipe or pipe-separated or newlines
    val instructionsRaw: String, // Newline separated step list
    val budgetTipsRaw: String, // Money saving secrets
    val cookingTipsRaw: String, // Pro chef tricks for cheap meals
    val tagsRaw: String, // "Vegetarian|High Protein|Quick|Pantry Ready"
    val drawableResName: String,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false
)
