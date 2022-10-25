package com.example.todoss5.ui.category

import com.example.todoss5.database.entity.CategoryEntity

interface CategoryInterface {
    fun onDeleteCategory(categoryEntity: CategoryEntity)

    fun onCategoryEmptyStateClicked()

    fun onCategorySelected(categoryEntity: CategoryEntity)

    fun onColorPickerSelected(priority: String)
}