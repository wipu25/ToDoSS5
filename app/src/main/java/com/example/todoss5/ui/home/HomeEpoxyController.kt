package com.example.todoss5.ui.home

import com.airbnb.epoxy.EpoxyController
import com.example.todoss5.arch.ToBuyViewModel
import com.example.todoss5.database.entity.ItemWithCategoryEntity
import com.example.todoss5.ui.addHeaderModel
import com.example.todoss5.ui.home.models.ItemEntityEpoxyModel

class HomeEpoxyController(
    private val itemEntityInterface: ItemEntityInterface
): EpoxyController() {

    var viewState: ToBuyViewModel.HomeViewState = ToBuyViewModel.HomeViewState(isLoading = true)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if(viewState.isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }
        if(viewState.dataList.isEmpty()){
            EmptyEpoxyModel().id("empty_state").addTo(this)
            return
        }
        viewState.dataList.forEach {
            dataItem ->
            if(dataItem.isHeader) {
                addHeaderModel(dataItem.data as String)
                return@forEach
            }

            val itemWithCategoryEntity = dataItem.data as ItemWithCategoryEntity
            ItemEntityEpoxyModel(itemWithCategoryEntity, itemEntityInterface)
                .id(itemWithCategoryEntity.itemEntity.id)
                .addTo(this)
        }
    }
}