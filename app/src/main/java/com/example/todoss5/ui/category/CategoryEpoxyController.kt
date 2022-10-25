package com.example.todoss5.ui.category

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyController
import com.example.todoss5.R
import com.example.todoss5.SharedPrefUtil
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.databinding.ModelCategoryBinding
import com.example.todoss5.databinding.ModelEmptyButtonBinding
import com.example.todoss5.databinding.ModelPriorityColorItemBinding
import com.example.todoss5.ui.addHeaderModel
import com.example.todoss5.ui.epoxy.ViewBindingKotlinModel
import java.util.*

class CategoryEpoxyController(private val categoryInterface: CategoryInterface) : EpoxyController() {

    var categories : List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        addHeaderModel("Category")

        categories.forEach{
            CategoryEpoxyModel(it,categoryInterface).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category",categoryInterface)
            .id("add_category")
            .addTo(this)

        val highPriorityColor = SharedPrefUtil.getHighPriorityColor()
        val mediumPriorityColor = SharedPrefUtil.getMediumPriorityColor()
        val lowPriorityColor = SharedPrefUtil.getLowPriorityColor()

        ColorPickerEpoxyModel("High",highPriorityColor,categoryInterface).id("high").addTo(this)
        ColorPickerEpoxyModel("Medium",mediumPriorityColor,categoryInterface).id("medium").addTo(this)
        ColorPickerEpoxyModel("Low",lowPriorityColor,categoryInterface).id("low").addTo(this)
    }

    data class CategoryEpoxyModel(val categoryEntity: CategoryEntity,val categoryInterface: CategoryInterface)
        : ViewBindingKotlinModel<ModelCategoryBinding>(R.layout.model_category) {
        override fun ModelCategoryBinding.bind() {
            categoryText.text = categoryEntity.name

            root.setOnLongClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("Delete ${categoryText.text}")
                    .setPositiveButton("Yes") { _,_ ->
                        categoryInterface.onDeleteCategory(categoryEntity)
                    }
                    .setNegativeButton("Cancel") { _,_ ->
                    }.show()
                return@setOnLongClickListener true
            }
        }
    }

    data class EmptyButtonEpoxyModel(val buttonText: String, val categoryInterface: CategoryInterface)
        : ViewBindingKotlinModel<ModelEmptyButtonBinding>(R.layout.model_empty_button) {
        override fun ModelEmptyButtonBinding.bind() {
            emptyButton.text = buttonText
            emptyButton.setOnClickListener {categoryInterface.onCategoryEmptyStateClicked()}
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }

    data class ColorPickerEpoxyModel(val title: String,val color: Int, val categoryInterface: CategoryInterface) :
        ViewBindingKotlinModel<ModelPriorityColorItemBinding>(R.layout.model_priority_color_item) {
        override fun ModelPriorityColorItemBinding.bind() {
            val priority = title.toLowerCase()
            titleText.text = title
            priorityTextView.setBackgroundColor(color)
            root.setOnClickListener{ categoryInterface.onColorPickerSelected(priority) }
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }
}