package com.example.todoss5.ui.home.bottomSheet

import com.airbnb.epoxy.EpoxyController
import com.example.todoss5.R
import com.example.todoss5.arch.ToBuyViewModel
import com.example.todoss5.databinding.ModelSortOrderItemBinding
import com.example.todoss5.ui.epoxy.ViewBindingKotlinModel

class BottomSheetEpoxyController(
    private val sortOptions: Array<ToBuyViewModel.HomeViewState.Sort>,
    private val selectedCallback: (ToBuyViewModel.HomeViewState.Sort) -> Unit
): EpoxyController() {
    override fun buildModels() {
        sortOptions.forEach {
            SortOrderItemEpoxyModel(it, selectedCallback).id(it.displayName).addTo(this)
        }
    }

    data class SortOrderItemEpoxyModel(
        val sort: ToBuyViewModel.HomeViewState.Sort,
        val selectedCallback: (ToBuyViewModel.HomeViewState.Sort) -> Unit
    ): ViewBindingKotlinModel<ModelSortOrderItemBinding>(R.layout.model_sort_order_item){
        override fun ModelSortOrderItemBinding.bind() {
            sortTitle.text = sort.displayName
            root.setOnClickListener{ selectedCallback(sort) }
        }
    }
}