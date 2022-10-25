package com.example.todoss5.database.dao

import androidx.room.*
import com.example.todoss5.database.entity.ItemEntity
import com.example.todoss5.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemEntityDao {
    @Query("SELECT * FROM item_entity")
    fun getAll(): Flow<List<ItemEntity>>

    @Transaction
    @Query("SELECT * FROM item_entity")
    fun getAllItemWithCategoryEntities() : Flow<List<ItemWithCategoryEntity>>

    @Insert
    suspend fun insert(itemEntity: ItemEntity)

    @Delete
    suspend fun delete(itemEntity: ItemEntity)

    @Update
    suspend fun update(itemEntity: ItemEntity)
}