package com.example.todoss5.ui.home.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.todoss5.arch.ToBuyViewModel
import com.example.todoss5.databinding.BottomSheetSortOrderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortOrderBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSortOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ToBuyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetSortOrderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val epoxyController = BottomSheetEpoxyController(ToBuyViewModel.HomeViewState.Sort.values()) {
            viewModel.currentSort = it
            dismiss()
        }

        //since we didn't observe on item build model will run once with this method
        binding.sortOrderEpoxy.setControllerAndBuildModels(epoxyController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}