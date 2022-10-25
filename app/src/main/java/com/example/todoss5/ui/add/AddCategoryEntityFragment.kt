package com.example.todoss5.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.databinding.FragmentAddCategoryBinding
import com.example.todoss5.ui.BaseFragment
import java.util.*

class AddCategoryEntityFragment : BaseFragment() {
    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryButton.setOnClickListener {
            saveCategoryToDatabase()
        }
        binding.categoryEditText.requestFocus()

        sharedViewModel.categorySaveCompleteLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                Toast.makeText(context,"Category Added",Toast.LENGTH_SHORT).show()
                navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun  saveCategoryToDatabase() {
        val category = binding.categoryEditText.text.toString().trim()
        val id = UUID.randomUUID().toString()

        if(category.isEmpty()) {
            binding.categoryEdit.error = "* Category required"
            return
        }

        val categoryEntity = CategoryEntity(
            name = category,
            id = id
        )
        sharedViewModel.insertCategory(categoryEntity)
    }
}