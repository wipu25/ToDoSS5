package com.example.todoss5.ui.home.models

import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.todoss5.R
import com.example.todoss5.SharedPrefUtil
import com.example.todoss5.database.entity.ItemWithCategoryEntity
import com.example.todoss5.databinding.ModelItemEntityBinding
import com.example.todoss5.ui.epoxy.ViewBindingKotlinModel
import com.example.todoss5.ui.home.ItemEntityInterface
import java.util.*

data class ItemEntityEpoxyModel(
    val item: ItemWithCategoryEntity,
    val itemEntityInterface: ItemEntityInterface
): ViewBindingKotlinModel<ModelItemEntityBinding>(R.layout.model_item_entity){

    //binding item entity with model item entity xml
    override fun ModelItemEntityBinding.bind() {
        titleText.text = item.itemEntity.title
        quantityText.text = "Quantity ${item.itemEntity.quantity}"
        categoryText.text = "Category ${item.categoryEntity?.name ?: "None"}"

        if(item.itemEntity.description != null) {
            descriptionText.isVisible = true
            descriptionText.text = item.itemEntity.description
        } else {
            descriptionText.isGone = true
        }

        priorityTextView.setOnClickListener{
            itemEntityInterface.onBumpPriority(item.itemEntity)
        }

        root.setOnClickListener {
            itemEntityInterface.onItemSelected(item.itemEntity)
        }

        val color = when (item.itemEntity.priority){
            1 -> SharedPrefUtil.getLowPriorityColor()
            2 -> SharedPrefUtil.getMediumPriorityColor()
            3 -> SharedPrefUtil.getHighPriorityColor()
            else -> android.R.color.black
        }

        root.strokeColor = color
        priorityTextView.setBackgroundColor(color)
    }
}