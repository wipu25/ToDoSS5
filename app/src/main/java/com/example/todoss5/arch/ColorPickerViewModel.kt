package com.example.todoss5.arch

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoss5.SharedPrefUtil
import java.util.*

class ColorPickerViewModel: ViewModel() {

    data class ViewState(
        val red: Int = 0,
        val blue: Int = 0,
        val green: Int = 0,
        private val priorityName: String = ""
    ) {
        fun getFormattedTitle(): String {
            return "$priorityName ( $red, $green, $blue )"
        }
    }

    private lateinit var priorityName: String
    private val _viewStateLiveData = MutableLiveData<ViewState>()
    val viewStateLiveData:LiveData<ViewState> = _viewStateLiveData

    fun setPriorityName(priorityName: String,colorCallback: (Int,Int,Int) -> Unit){
        this.priorityName = priorityName

        val colorInt = when (priorityName.toLowerCase(Locale.US)){
            "low" -> SharedPrefUtil.getLowPriorityColor()
            "medium" -> SharedPrefUtil.getMediumPriorityColor()
            "high" -> SharedPrefUtil.getHighPriorityColor()
            else -> 0
        }

        val color = Color.valueOf(colorInt)
        _viewStateLiveData.postValue(
            ViewState(
            priorityName = priorityName,
            red = (color.red() * 255).toInt(),
            blue = (color.blue() * 255).toInt(),
            green = (color.green() * 255).toInt()
            )
        )
    }

    fun onRedChange(red: Int) {
        val viewState = _viewStateLiveData.value ?: ViewState()
        _viewStateLiveData.postValue(viewState.copy(red = red))
    }

    fun onBlueChange(blue: Int){
        val viewState = _viewStateLiveData.value ?: ViewState()
        _viewStateLiveData.postValue(viewState.copy(blue = blue))
    }

    fun onGreenChange(green: Int){
        val viewState = _viewStateLiveData.value ?: ViewState()
        _viewStateLiveData.postValue(viewState.copy(green = green))
    }
}