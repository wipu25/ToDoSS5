package com.example.todoss5.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoss5.database.AppDatabase
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.database.entity.ItemEntity
import com.example.todoss5.database.entity.ItemWithCategoryEntity
import com.example.todoss5.ui.addHeaderModel
import com.example.todoss5.ui.getHeaderTextForPriority
import com.example.todoss5.ui.home.models.ItemEntityEpoxyModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ToBuyViewModel: ViewModel() {

    private lateinit var repository: ToBuyRepository
    //interact with repository

    val itemEntitiesLiveData = MutableLiveData<List<ItemEntity>>()
    val itemWithCategoryEntityLiveData = MutableLiveData<List<ItemWithCategoryEntity>>()
    val categoryEntitiesLiveData = MutableLiveData<List<CategoryEntity>>()

    //for keeping state is transaction completely insert
    val transactionCompleteLiveData = MutableLiveData<Event<Boolean>>()
    val categorySaveCompleteLiveData = MutableLiveData<Event<Boolean>>()

    var currentSort: HomeViewState.Sort = HomeViewState.Sort.NONE
        set(value) {
            field = value
            updateHomeViewState(itemWithCategoryEntityLiveData.value!!)
        }
    //serve info to fragment
    private val _homeViewStateLiveData = MutableLiveData<HomeViewState>()
    val homeViewStateLiveData: LiveData<HomeViewState>
        get() = _homeViewStateLiveData

    //not let view layer to modified mutable live data
    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData: LiveData<CategoriesViewState>
        get() = _categoriesViewStateLiveData

    fun init(appDatabase: AppDatabase){
        repository = ToBuyRepository(appDatabase)

        //scope of activity viewmodel
        viewModelScope.launch {
            //get new data snapshot everytime data change
            repository.getAllItems().collect{
                itemEntitiesLiveData.postValue(it)
            }
            //can't continue here need to create new scope
        }
        viewModelScope.launch {
            //get new data snapshot everytime data change
            repository.getAllItemWithCategoryEntities().collect{
                itemWithCategoryEntityLiveData.postValue(it)

                updateHomeViewState(it)
            }
            //can't continue here need to create new scope
        }
        viewModelScope.launch {
            repository.getAllCategory().collect{
                categoryEntitiesLiveData.postValue(it)
            }
        }
    }

    private fun updateHomeViewState(item: List<ItemWithCategoryEntity>) {
        val dataList = ArrayList<HomeViewState.DataItem<*>>()
        when(currentSort) {
            HomeViewState.Sort.NONE -> {
                var currentPriority = -1
                //Add item to epoxy recycler list
                item.sortedByDescending {
                    it.itemEntity.priority
                }.forEach {item ->
                    //add header if new priority
                    if(item.itemEntity.priority != currentPriority){
                        currentPriority = item.itemEntity.priority

                        //add header
                        val headerItem = HomeViewState.DataItem(
                            data = getHeaderTextForPriority(currentPriority),
                            isHeader = true
                        )
                        dataList.add(headerItem)
                    }
                    //add normal list
                    val dataItem = HomeViewState.DataItem(data = item)
                    dataList.add(dataItem)
                }
            }
            HomeViewState.Sort.CATEGORY -> {
                var currentCategory = "no_id"
                item.sortedBy { it.itemEntity.categoryId }.forEach {
                    if(it.itemEntity.categoryId != currentCategory){
                        currentCategory = it.itemEntity.categoryId
                        val headerItem = HomeViewState.DataItem(
                            data = it.categoryEntity?.name ?: "None",
                            isHeader = true
                        )
                        dataList.add(headerItem)
                    }
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }
            HomeViewState.Sort.NEWEST -> {
                val headerItem = HomeViewState.DataItem(
                    data = "Newest",
                    isHeader = true
                )
                dataList.add(headerItem)
                item.sortedBy{it.itemEntity.createdAt}.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }
            HomeViewState.Sort.OLDEST -> {
                val headerItem = HomeViewState.DataItem(
                    data = "Oldest",
                    isHeader = true
                )
                dataList.add(headerItem)
                item.sortedByDescending{it.itemEntity.createdAt}.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }
        }
        _homeViewStateLiveData.postValue(
            HomeViewState(
                dataList = dataList,
                isLoading = false,
                sort = currentSort
            )
        )
    }

    //region Category
    fun onCategorySelected(categoryId: String,showLoading: Boolean = false) {
        if(showLoading){
            val loadingViewState = CategoriesViewState(isLoading = true)
            _categoriesViewStateLiveData.value = loadingViewState
        }

        //get recent categories from live data
        val categories = categoryEntitiesLiveData.value ?: return

        //update list when selected smth
        val viewStateItemList = ArrayList<CategoriesViewState.Item>()

        viewStateItemList.add(CategoriesViewState.Item(
            categoryEntity = CategoryEntity.getDefaultCategory(),
            isSelected = categoryId == CategoryEntity.DEFAULT_CATEGORY_ID
        ))

        //loop through categories and add state of category to list
        categories.forEach {
            viewStateItemList.add(CategoriesViewState.Item(
                categoryEntity = it,
                //if id == selected enable this item
                isSelected = it.id == categoryId
            ))
        }

        //post to let view known updated value
        val viewState = CategoriesViewState(itemList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)
    }

    data class CategoriesViewState (
        val isLoading: Boolean = false,
        //wrapper of the entity
        val itemList: List<Item> = emptyList()
    ) {
        data class Item (
            val categoryEntity: CategoryEntity = CategoryEntity(),
            val isSelected: Boolean = false
        )

        fun getSelectedCategoryId() : String {
            return itemList.find { it.isSelected }?.categoryEntity?.id ?: CategoryEntity.DEFAULT_CATEGORY_ID
        }
    }
    //endregion category

    //all info of particular home screen
    data class HomeViewState(
        //list of obj
        val dataList: List<DataItem<*>> = emptyList(),
        val isLoading: Boolean = false,
        val sort: Sort = Sort.NONE
    ) {
        data class DataItem<T> (
            val data: T,
            val isHeader: Boolean = false
        )

        enum class Sort(val displayName: String) {
            NONE("None"),
            CATEGORY("Category"),
            OLDEST("Oldest"),
            NEWEST("Newest"),
        }
    }

    //region ItemEntity
    fun insertItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.insertItem(itemEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(itemEntity)
        }
    }

    fun updateItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.updateItem(itemEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }
    //endregion ItemEntity

    //region CategoryEntity
    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)
            categorySaveCompleteLiveData.postValue(Event(true))
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(categoryEntity)
            categorySaveCompleteLiveData.postValue(Event(true))
        }
    }
    //endregion CategoryEntity
}