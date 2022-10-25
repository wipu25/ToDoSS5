package com.example.todoss5.ui

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyController
import com.example.todoss5.ui.home.models.HeaderEpoxyModel
import com.google.android.material.color.MaterialColors

fun EpoxyController.addHeaderModel(headerText: String) {
    //having this required extension from class
    HeaderEpoxyModel(headerText).id(headerText).addTo(this)
}

fun getHeaderTextForPriority(priority: Int): String{
    return when (priority) {
        1 -> "Low"
        2 -> "Medium"
        3 -> "High"
        else -> "None"
    }
}

@ColorInt
fun View.getAttrColor(attrResId: Int): Int {
    return ContextCompat.getColor(context,attrResId)
}