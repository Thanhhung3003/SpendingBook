package com.example.appmoney.ui.main.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScreenHomeViewModel: ViewModel() {
    private val repository = Repository()

    private val _incomeList = MutableLiveData<List<Category>>()
    val incomeList: LiveData<List<Category>> = _incomeList

    private val _expList = MutableLiveData<List<Category>>()
    val expList: LiveData<List<Category>> = _expList

    // getter
    fun getIncomeCat(onFailure: (String) -> Unit) {
        repository.getCategory("Income", { list ->
            _incomeList.value = list
        }, onFailure)
    }

    fun getExpenditureCat(onFailure: (String) -> Unit) {
        repository.getCategory("Expenditure", { list ->
            _expList.value = list
        }, onFailure)
    }
}