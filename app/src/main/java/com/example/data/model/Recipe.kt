package com.example.data.model

import com.example.data.local.RecipeEntity

data class Recipe(
    val id: String,
    val title: String,
    val category: String, // "Lunch", "Dinner", "Lunch & Dinner"
    val estimatedCost: Double,
    val totalCost: Double,
    val servings: Int,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: String,
    val calories: Int,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val budgetTips: List<String>,
    val cookingTips: List<String>,
    val tags: List<String>,
    val drawableResName: String,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false
) {
    companion object {
        fun fromEntity(entity: RecipeEntity): Recipe {
            return Recipe(
                id = entity.id,
                title = entity.title,
                category = entity.category,
                estimatedCost = entity.estimatedCost,
                totalCost = entity.totalCost,
                servings = entity.servings,
                prepTimeMinutes = entity.prepTimeMinutes,
                cookTimeMinutes = entity.cookTimeMinutes,
                difficulty = entity.difficulty,
                calories = entity.calories,
                description = entity.description,
                ingredients = entity.ingredientsRaw.split("|").filter { it.isNotBlank() },
                instructions = entity.instructionsRaw.split("|").filter { it.isNotBlank() },
                budgetTips = entity.budgetTipsRaw.split("|").filter { it.isNotBlank() },
                cookingTips = entity.cookingTipsRaw.split("|").filter { it.isNotBlank() },
                tags = entity.tagsRaw.split("|").filter { it.isNotBlank() },
                drawableResName = entity.drawableResName,
                isFavorite = entity.isFavorite,
                isCustom = entity.isCustom
            )
        }
    }

    fun toEntity(): RecipeEntity {
        return RecipeEntity(
            id = id,
            title = title,
            category = category,
            estimatedCost = estimatedCost,
            totalCost = totalCost,
            servings = servings,
            prepTimeMinutes = prepTimeMinutes,
            cookTimeMinutes = cookTimeMinutes,
            difficulty = difficulty,
            calories = calories,
            description = description,
            ingredientsRaw = ingredients.joinToString("|"),
            instructionsRaw = instructions.joinToString("|"),
            budgetTipsRaw = budgetTips.joinToString("|"),
            cookingTipsRaw = cookingTips.joinToString("|"),
            tagsRaw = tags.joinToString("|"),
            drawableResName = drawableResName,
            isFavorite = isFavorite,
            isCustom = isCustom
        )
    }
}
