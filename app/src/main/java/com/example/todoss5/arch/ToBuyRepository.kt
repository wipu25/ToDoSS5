package com.example.todoss5.arch

import com.example.todoss5.database.AppDatabase
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.database.entity.ItemEntity
import com.example.todoss5.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

class ToBuyRepository(private val appDatabase : AppDatabase) {

    //interact with DB
    //region itemEntity
    suspend fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().insert(itemEntity)
    }

    suspend fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    suspend fun updateItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().update(itemEntity)
    }

    fun getAllItems(): Flow<List<ItemEntity>> {
        return appDatabase.itemEntityDao().getAll()
    }

    fun getAllItemWithCategoryEntities(): Flow<List<ItemWithCategoryEntity>> {
        return appDatabase.itemEntityDao().getAllItemWithCategoryEntities()
    }
    //endregion ItemEntity

    //region CategoryEntity
    suspend fun insertCategory(CategoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().insert(CategoryEntity)
    }

    suspend fun deleteCategory(CategoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().delete(CategoryEntity)
    }

    suspend fun updateCategory(CategoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().update(CategoryEntity)
    }

    fun getAllCategory(): Flow<List<CategoryEntity>> {
        return appDatabase.categoryEntityDao().getAll()
    }
    //endregion CategoryEntity
}