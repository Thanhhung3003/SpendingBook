package com.example.appmoney.ui.input.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category

class InComeViewModel : ViewModel() {
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

    fun init(categories: List<Category>) {
        _categories.value = categories
    }
}