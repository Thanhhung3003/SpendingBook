package com.example.appmoney.ui.main.feature.input

import com.example.appmoney.data.model.Category

interface InputFragmentBehavior {

    fun setSelectedCategoryById(categoryId: String)

    fun getSelectedCategory(): Category?
}