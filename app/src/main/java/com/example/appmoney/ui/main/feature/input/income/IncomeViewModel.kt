package com.example.appmoney.ui.main.feature.input.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category

class IncomeViewModel: ViewModel() {

    private val _categories = MutableLiveData<List<Category>>(emptyList())
    val categories: LiveData<List<Category>> = _categories

    fun onSelectedCategory(category: Category) {
        _categories.value?.let { currentCategories ->
            val newList = currentCategories.map { categoryOnList ->
                if (categoryOnList == category) {
                    categoryOnList.copy(isSelected = true)
                } else {
                    categoryOnList.copy(isSelected = false)
                }
            }
            _categories.value = newList
        }
    }

    fun setSelectedCategoryById(categoryId: String) {
        _categories.value?.find { it.idCat == categoryId }?.let {
            onSelectedCategory(it)
        } ?: run {
            _categories.value = _categories.value?.map { it.copy(isSelected = false) }
        }
    }

    fun getSelectedCat(): Category?{
        return categories.value?.firstOrNull{it.isSelected}
    }

    fun init(categories: List<Category>) {
        _categories.value = categories
    }
}