package com.example.todoss5.ui.add

import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyController
import com.example.todoss5.R
import com.example.todoss5.arch.ToBuyViewModel
import com.example.todoss5.databinding.ModelAddCategoryItemBinding
import com.example.todoss5.ui.epoxy.ViewBindingKotlinModel
import com.example.todoss5.ui.getAttrColor
import com.example.todoss5.ui.home.LoadingEpoxyModel

class AddItemCategoryEpoxyController(
    val onCategorySelected : (String) -> Unit
) : EpoxyController() {

    var item = ToBuyViewModel.CategoriesViewState()
    set(value) {
        field = value
        requestModelBuild()
    }

    override fun buildModels() {
        if(item.isLoading) {
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }
        if(item.itemList.isNotEmpty()){
            item.itemList.forEach{ item ->
                AddCategoryEntityEpoxy(item, onCategorySelected).id(item.categoryEntity.id).addTo(this)
            }
        }
    }

    data class AddCategoryEntityEpoxy(
        val item: ToBuyViewModel.CategoriesViewState.Item,
        private val onCategorySelected : (String) -> Unit
    ) : ViewBindingKotlinModel<ModelAddCategoryItemBinding>(R.layout.model_add_category_item) {
        override fun ModelAddCategoryItemBinding.bind() {
            categoryName.text = item.categoryEntity.name
            root.setOnClickListener {
                onCategorySelected(item.categoryEntity.id)
            }

            var textColor = R.color.pink_irresist_dark
            var strokeColor = R.color.purple_200

            if(item.isSelected){
                textColor = R.color.blue_steel
                strokeColor = R.color.black
            }

            categoryName.setTextColor(root.getAttrColor(textColor))
            root.strokeColor = root.getAttrColor(strokeColor)
        }
    }
}