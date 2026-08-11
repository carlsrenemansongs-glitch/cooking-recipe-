package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Query("SELECT * FROM grocery_items ORDER BY isPurchased ASC, id DESC")
    fun getAllGroceryItems(): Flow<List<GroceryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryItem(item: GroceryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryItems(items: List<GroceryEntity>)

    @Query("UPDATE grocery_items SET isPurchased = :purchased WHERE id = :id")
    suspend fun updateGroceryStatus(id: Int, purchased: Boolean)

    @Delete
    suspend fun deleteGroceryItem(item: GroceryEntity)

    @Query("DELETE FROM grocery_items WHERE isPurchased = 1")
    suspend fun clearPurchased()
}
