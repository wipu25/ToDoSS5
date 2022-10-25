package com.example.todoss5.ui.add
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.todoss5.R
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.database.entity.ItemEntity
import com.example.todoss5.ui.BaseFragment
import com.example.todoss5.databinding.FragmentAddItemBinding
import java.util.*

class AddItemEntityFragment: BaseFragment() {
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: AddItemEntityFragmentArgs by navArgs()

    //edit item
    private val selectedItemEntity: ItemEntity? by lazy {
        sharedViewModel.itemEntitiesLiveData.value?.find{
            it.id == safeArgs.selectedItemEntityId
        }
    }

    private var isInEditMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveItemEntityToDataBase()
        }

        binding.quantitySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.quantityValue.text = p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){ event ->
            event.getContent()?.let {
                if(isInEditMode){
                    Toast.makeText(requireActivity(),"Item Updated",Toast.LENGTH_SHORT).show()
                    navigateUp()
                    return@observe
                }
                Toast.makeText(requireActivity(),"Item Saved",Toast.LENGTH_SHORT).show()
                binding.titleEditText.text = null
                binding.titleEditText.requestFocus()
                mainActivity.hideKeyboard(view)

                binding.descriptionEditText.text = null
                binding.radioGroup.check(R.id.radioLow)
            }
        }

        //show keyboard by default
        mainActivity.showKeyboard()
        binding.titleEditText.requestFocus()

        //setup screen in edit mode
        selectedItemEntity?.let {
            isInEditMode = true
            binding.quantitySeekbar.progress = (it.quantity)
            binding.quantityValue.text = it.quantity.toString()
            binding.titleEditText.setText(it.title)
            binding.descriptionEditText.setText(it.description)
            when (it.priority) {
                1 -> binding.radioGroup.check(R.id.radioLow)
                2 -> binding.radioGroup.check(R.id.radioMedium)
                3 -> binding.radioGroup.check(R.id.radioHigh)
            }
            binding.quantitySeekbar.setProgress(it.quantity,false)
            binding.saveButton.text = "Update"
            mainActivity.supportActionBar?.title = "Update Item"
        }

        sharedViewModel.onCategorySelected(selectedItemEntity?.categoryId ?: CategoryEntity.DEFAULT_CATEGORY_ID,true)

        val selectItemCategoryEpoxyController = AddItemCategoryEpoxyController{ categoryId ->
            sharedViewModel.onCategorySelected(categoryId)
        }
        binding.categoryList.setController(selectItemCategoryEpoxyController)

        //listen for change on category selected
        sharedViewModel.categoriesViewStateLiveData.observe(viewLifecycleOwner) {
            categoryItems -> selectItemCategoryEpoxyController.item = categoryItems
        }
    }

    private fun saveItemEntityToDataBase() {
        val itemTitle = binding.titleEditText.text.toString().trim()
        val descriptionTitle = binding.descriptionEditText.text.toString().trim()
        val priority = when( binding.radioGroup.checkedRadioButtonId) {
            R.id.radioLow -> 1
            R.id.radioMedium -> 2
            R.id.radioHigh -> 3
            else -> 0
        }
        val quantity = binding.quantitySeekbar.progress
        val categoryId = sharedViewModel.categoriesViewStateLiveData.value?.getSelectedCategoryId() ?: return

        if(itemTitle.isEmpty()){
            binding.titleEdit.error = "Please provide title"
            return
        }

        if(isInEditMode) {
            val itemEntity = selectedItemEntity!!.copy(
                title = itemTitle,
                description = descriptionTitle,
                priority = priority,
                quantity = quantity,
                categoryId = categoryId
            )
            sharedViewModel.updateItem(itemEntity)
            return
        }

        val id = UUID.randomUUID().toString()
        val itemEntity = ItemEntity(
            id = id,
            title = itemTitle,
            description = descriptionTitle,
            priority = priority,
            quantity = quantity,
            createdAt = System.currentTimeMillis(),
            categoryId = categoryId //todo update this later when category available
        )

        sharedViewModel.insertItem(itemEntity)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}