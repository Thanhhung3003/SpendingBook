package com.example.appmoney.ui.main.feature.input

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TabObject

class InputViewModel : ViewModel() {
    private val repo = Repository()

    private val _err = MutableLiveData<String?>()
    val err: LiveData<String?> = _err

    private val _state = MutableLiveData(InputState())
    val state: LiveData<InputState> = _state

    fun clearErr() {
        _err.value = null
    }

    fun updateState(newState: InputState?) {
        _state.value = newState
    }

    private val _selectedTab = MutableLiveData<Int>()
    val selectedTab: LiveData<Int> = _selectedTab

    fun setTab(tab: Int) {
        _selectedTab.value = tab

        TabObject.changeTab(tab)
    }

    fun addTrans(
        sCategoryId: String,
        sDate: String,
        sAmount: Long,
        sNote: String,
        typeTrans: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        if (sAmount.toString().isEmpty()) {
            _err.value = "Bạn chưa nhập số tiền"
            return
        }
        if (sCategoryId == null) {
            _err.value = "Bạn chưa chọn danh mục"
            return
        }
        val transaction = Transaction(
            date = sDate,
            amount = sAmount,
            note = sNote,
            categoryId = sCategoryId,
            typeTrans = typeTrans,
        )

        repo.addTrans(transaction, onSuccess, onFailure)
    }

}