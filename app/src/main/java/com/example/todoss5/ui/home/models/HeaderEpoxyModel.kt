package com.example.todoss5.ui.home.models

import com.example.todoss5.R
import com.example.todoss5.databinding.ModelHeaderItemBinding
import com.example.todoss5.ui.epoxy.ViewBindingKotlinModel


data class HeaderEpoxyModel(val headerText: String): ViewBindingKotlinModel<ModelHeaderItemBinding>(
    R.layout.model_header_item) {
    override fun ModelHeaderItemBinding.bind() {
        headerTitle.text = headerText
        val color = when (headerText) {
            "Low" -> android.R.color.holo_blue_light
            "Medium" -> android.R.color.holo_orange_light
            "High" -> android.R.color.holo_red_light
            else -> android.R.color.black
        }
//            root.setBackgroundColor(ContextCompat.getColor(root.context,color))
    }

    //span full width screen
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}