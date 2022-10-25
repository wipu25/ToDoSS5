package com.example.todoss5.ui.colorPicker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todoss5.SharedPrefUtil
import com.example.todoss5.arch.ColorPickerViewModel
import com.example.todoss5.databinding.FragmentColorPickerBinding
import com.example.todoss5.ui.BaseFragment
import java.util.*

class ColorPickerFragment : BaseFragment() {
    private var _binding: FragmentColorPickerBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: ColorPickerFragmentArgs by navArgs()
    private val viewModel: ColorPickerViewModel by viewModels()

    private class SeekBarListener(
        private val onChange: (Int) -> Unit
    ) : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            onChange(p1)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColorPickerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPriorityName(safeArgs.priorityName) { red,green,blue ->
            binding.sliderPickerRed.seekBar.progress = red
            binding.sliderPickerGreen.seekBar.progress = green
            binding.sliderPickerBlue.seekBar.progress = blue
        }


        binding.sliderPickerBlue.apply {
            textView.text = "Blue"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onBlueChange))
        }

        binding.sliderPickerRed.apply {
            textView.text = "Red"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onRedChange))
        }
        binding.sliderPickerGreen.apply {
            textView.text = "Green"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onGreenChange))
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner){ viewState ->
            binding.customPickerTitle.text = viewState.getFormattedTitle()

            binding.colorDisplay.setBackgroundColor(Color.rgb(viewState.red,viewState.green,viewState.blue))
        }

        binding.saveButton.setOnClickListener {
            val viewState = viewModel.viewStateLiveData.value ?: return@setOnClickListener
            val color = Color.rgb(viewState.red, viewState.green, viewState.blue)
            when(safeArgs.priorityName.toLowerCase(Locale.US)) {
                "low" -> SharedPrefUtil.setLowPriorityColor(color)
                "medium" -> SharedPrefUtil.setMediumPriorityColor(color)
                "high" -> SharedPrefUtil.setHighPriorityColor(color)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}