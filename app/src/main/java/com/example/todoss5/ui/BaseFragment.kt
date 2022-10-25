package com.example.todoss5.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.todoss5.arch.ToBuyViewModel
import com.example.todoss5.database.AppDatabase
import com.google.android.material.color.MaterialColors


abstract class BaseFragment: Fragment() {
    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDatabase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    protected val sharedViewModel: ToBuyViewModel by activityViewModels()

    protected fun navigateViaNavGraph(actionId: Int) {
        mainActivity.navController.navigate(actionId)
    }

    protected fun navigateViaNavGraph(navDirections: NavDirections) {
        mainActivity.navController.navigate(navDirections)
    }

    protected fun navigateUp() {
        mainActivity.navController.navigateUp()
    }


    //to get color without calling everytime
    @ColorInt
    protected fun getAttrColor(attrResId: Int): Int {
        return MaterialColors.getColor(requireView(), attrResId)
    }
}