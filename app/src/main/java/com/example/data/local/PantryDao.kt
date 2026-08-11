package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PantryDao {
    @Query("SELECT * FROM pantry_items ORDER BY category ASC, name ASC")
    fun getAllPantryItems(): Flow<List<PantryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryItems(items: List<PantryEntity>)

    @Query("UPDATE pantry_items SET isInPantry = :inPantry WHERE id = :id")
    suspend fun updatePantryStatus(id: Int, inPantry: Boolean)

    @Query("SELECT COUNT(*) FROM pantry_items")
    suspend fun getPantryCount(): Int
}
