package com.example.appmoney.ui.main.feature.transactionhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.TransAndCat
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.data.repository.Repository

class HistoryTransViewModel: ViewModel() {

    private val repository = Repository()

    private var transactions = mutableListOf <Transaction>()

    private val _transactionsDetail = MutableLiveData<List<TransactionDetail>>()
    val transactionsDetail: LiveData<List<TransactionDetail>> = _transactionsDetail

    fun getTrans(listCategory: List<Category>, onFailure:(String)->Unit){
        repository.getTransWithCat(onSuccess = { transactions ->
            val transactionsDetail = mutableListOf<TransactionDetail>()
            listCategory.forEach { category ->
                val listTrans = transactions.filter { it.categoryId == category.idCat }
                val trans = listTrans.map { it.toDetail(category) }
                transactionsDetail.addAll(trans)
            }
            val unknowTrans = transactions.filter { trans ->
                listCategory.none { it.idCat == trans.categoryId }
            }
            val unknowDetail = unknowTrans.map { it.toDetail(null) }
            transactionsDetail.addAll(unknowDetail)
            _transactionsDetail.value = transactionsDetail
        }, onFailure)
    }
}

fun Transaction.toDetail(category: Category?): TransactionDetail {
    return TransactionDetail(
        id = id,
        amount = amount,
        date = date,
        note = note,
        categoryId = categoryId,
        typeTrans = typeTrans,
        image = category?.image,
        color = category?.color,
        desCat = category?.desCat
    )
}