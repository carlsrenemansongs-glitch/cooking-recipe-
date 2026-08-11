package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_items")
data class GroceryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val estimatedCost: Double,
    val isPurchased: Boolean = false,
    val recipeTitle: String? = null
)
