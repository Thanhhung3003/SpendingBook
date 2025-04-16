package com.example.appmoney.ui.main.feature.input

import com.example.appmoney.data.model.Category

interface CategorySelectable {
    fun getSelectedCategory(): Category?
}