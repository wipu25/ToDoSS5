package com.example.todoss5.ui.home

import android.os.Bundle
import android.view.*
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.todoss5.R
import com.example.todoss5.database.entity.ItemEntity
import com.example.todoss5.database.entity.ItemWithCategoryEntity
import com.example.todoss5.ui.BaseFragment
import com.example.todoss5.databinding.FragmentHomeBinding
import com.example.todoss5.ui.home.bottomSheet.SortOrderBottomSheetDialogFragment
import com.example.todoss5.ui.home.models.ItemEntityEpoxyModel

class HomeFragment: BaseFragment(),ItemEntityInterface {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener{
            navigateViaNavGraph(R.id.action_homeFragment_to_addItemEntityFragment)
        }

        val controller = HomeEpoxyController(this)
        binding.epoxyRecyclerView.setController(controller)

        sharedViewModel.homeViewStateLiveData.observe(viewLifecycleOwner) {
            viewState -> controller.viewState = viewState
        }

        EpoxyTouchHelper.initSwiping(binding.epoxyRecyclerView)
            .right()
            .withTarget(ItemEntityEpoxyModel::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<ItemEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: ItemEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val itemThatWasRemoved = model?.item?.itemEntity ?: return
                    sharedViewModel.deleteItem(itemThatWasRemoved)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.menu_home_sort) {
            SortOrderBottomSheetDialogFragment().show(childFragmentManager, null)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBumpPriority(itemEntity: ItemEntity) {
        var newPriority = itemEntity.priority + 1
        if(itemEntity.priority == 3){
            newPriority = 1
        }
        sharedViewModel.updateItem(itemEntity.copy(priority = newPriority))
    }

    override fun onItemSelected(itemEntity: ItemEntity) {
        //edit item with passing id
        val navDirections =
            HomeFragmentDirections.actionHomeFragmentToAddItemEntityFragment(itemEntity.id)
        navigateViaNavGraph(navDirections)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}