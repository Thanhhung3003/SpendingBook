package com.example.appmoney.ui.main.feature.fixcategory

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.R
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryColorWrapper
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.CategoryImageWrapper
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TabObject

class FixViewModel : ViewModel() {

    private val repository = Repository()

    private val _category = MutableLiveData<Category>()
    val category: LiveData<Category> get() = _category

    private val _colors: MutableLiveData<List<CategoryColorWrapper>> = MutableLiveData()
    val colors: LiveData<List<CategoryColorWrapper>> = _colors

    private val _images: MutableLiveData<List<CategoryImageWrapper>> = MutableLiveData()
    val images: LiveData<List<CategoryImageWrapper>> = _images

    private val _err = MutableLiveData<String?>()
    val err: LiveData<String?> = _err

    fun setCat( category: Category){
        _category.value = category
    }

    fun clearErr() {
        _err.value = null
    }

    fun updateCat(
        typeId: String,
        sName: String,
        cat: Category,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val sImage = getImageSelected()
        val sColor = getColorSelected()


        if (sImage == null) {
            _err.value = "Vui lòng chọn biểu tượng"
            return
        }

        if (sColor == null) {
            _err.value = "Vui lòng chọn màu"
            return
        }

        if (sName.isEmpty()) {
            _err.value = "Vui lòng nhập tên danh mục"
            return
        }
        val cat = Category(
            image = sImage,
            color = sColor,
            desCat = sName,
            timeCreate = System.currentTimeMillis()
        )

        repository.updateCategory(
            typeId, cat.idCat, cat,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    fun onSelectedCategoryColor(categoryColor: CategoryColor) {
        val newList = colors.value?.map { categoryOnList ->
            categoryOnList.copy(isSelected = categoryOnList.categoryColor == categoryColor)
        }
            _colors.value = newList ?: emptyList()

    }

    fun onSelectedCategoryImage(categoryImage: CategoryImage) {
        val newList = images.value?.map { imageOnList ->
            imageOnList.copy(isSelected = imageOnList.categoryImage == categoryImage)
        }
        _images.value = newList ?: emptyList()
    }

    fun loadItemsImage (images: List<CategoryImage>) {
        _images.value = images.map { CategoryImageWrapper(it, false) }
    }
    fun loadItemsColor(colors: List<CategoryColor>) {
        _colors.value = colors.map { CategoryColorWrapper(it, false) }
    }
    fun getColorSelected(): CategoryColor? {
        return colors.value?.firstOrNull { it.isSelected }?.categoryColor
    }
    fun getImageSelected(): CategoryImage? {
        return images.value?.firstOrNull { it.isSelected }?.categoryImage
    }


}