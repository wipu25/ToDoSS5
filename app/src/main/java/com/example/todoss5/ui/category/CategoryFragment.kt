package com.example.todoss5.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoss5.R
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.databinding.FragmentCategoryBinding
import com.example.todoss5.ui.BaseFragment

class CategoryFragment: BaseFragment(), CategoryInterface {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val profileEpoxyController = CategoryEpoxyController(
        categoryInterface = this
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.epoxyRecyclerView.setController(profileEpoxyController)
        sharedViewModel.categoryEntitiesLiveData.observe(viewLifecycleOwner) {
            categoryEntityList -> profileEpoxyController.categories = categoryEntityList
        }

        binding.epoxyRecyclerView.postDelayed({
//            navigateViaNavGraph(Cate)
        }, 500)
    }

    override fun onDeleteCategory(categoryEntity: CategoryEntity) {
        sharedViewModel.deleteCategory(categoryEntity)
    }

    override fun onCategoryEmptyStateClicked() {
        //be here since we need navigateViaNavGraph
        navigateViaNavGraph(R.id.addCategoryItem)
    }

    override fun onColorPickerSelected(priority: String) {
        //be here since we need navigateViaNavGraph
        val navDirections =
            CategoryFragmentDirections.actionProfileFragmentToColorPicker(priority)
        navigateViaNavGraph(navDirections)
    }

    override fun onCategorySelected(categoryEntity: CategoryEntity) {
        //todo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}