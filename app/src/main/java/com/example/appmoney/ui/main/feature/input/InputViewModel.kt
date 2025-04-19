package com.example.appmoney.ui.main.feature.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TabObject
import com.example.appmoney.ui.common.helper.TimeFormat
import com.example.appmoney.ui.common.helper.TimeHelper
import java.util.Calendar

class InputViewModel : ViewModel() {
    private val repo = Repository()

    private val _err = MutableLiveData<String?>()
    val err: LiveData<String?> = _err

    private val _state = MutableLiveData(TransactionState())
    val state: LiveData<TransactionState> = _state

    fun clearErr() {
        _err.value = null
    }

    fun updateState(newState: TransactionState?) {
        _state.value = newState
    }

    private val _selectedTab = MutableLiveData<Int>()
    val selectedTab: LiveData<Int> = _selectedTab

    fun setTab(tab: Int) {
        _selectedTab.value = tab

        TabObject.changeTab(tab)
    }

    fun handleDoneButton(
        sCategoryId: String,
        typeTrans: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val date = state.value?.date ?: Calendar.getInstance()
        val sDate = TimeHelper.getByFormat(date, TimeFormat.Date)
        val sAmount = state.value?.amount ?: 0L
        val sNote = state.value?.note ?: ""
        when (val idTrans = state.value?.idTrans) {
            null ->
                addTrans(
                    sCategoryId,
                    sDate,
                    sAmount,
                    sNote,
                    typeTrans,
                    onSuccess, onFailure
                )

            else ->
                updateTrans(
                    idTrans,
                    sCategoryId,
                    sDate,
                    sAmount,
                    sNote,
                    typeTrans,
                    onSuccess, onFailure
                )
        }
    }

    private fun updateTrans(
        idTrans: String,
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
        repo.updateTrans(idTrans, transaction, onSuccess, onFailure)
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