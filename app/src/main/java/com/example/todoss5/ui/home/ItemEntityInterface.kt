package com.example.todoss5.ui.home

import com.example.todoss5.database.entity.ItemEntity

//each item function
interface ItemEntityInterface {
    fun onBumpPriority(itemEntity: ItemEntity)

    fun onItemSelected(itemEntity: ItemEntity)


}