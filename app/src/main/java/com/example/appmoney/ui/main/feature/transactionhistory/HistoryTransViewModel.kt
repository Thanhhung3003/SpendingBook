package com.example.appmoney.ui.main.feature.transactionhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.TransAndCat
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TimeHelper
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryTransViewModel: ViewModel() {

    private val repository = Repository()

    private var allTransactions = mutableListOf<TransactionDetail>()

    private val _transactionsDetail = MutableLiveData<List<TransactionDetail>>()
    val transactionsDetail: LiveData<List<TransactionDetail>> = _transactionsDetail

    fun getTrans(listCategory: List<Category>, onFailure:(String)->Unit){
        repository.getTransWithCat(onSuccess = { transactions ->
            val transactionDetails = transactions.map { trans ->
                val matchedCategory = listCategory.find { it.idCat == trans.categoryId }
                trans.toDetail(matchedCategory)
            }

            _transactionsDetail.value = transactionDetails
            allTransactions = transactionDetails.toMutableList()

        }, onFailure)
    }
    fun getTransByMonth(input: String,listCategory: List<Category>,onFailure: (String) -> Unit){
        try {
            val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            val date = sdf.parse(input) ?: return
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.DAY_OF_MONTH,1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val dateStart = Timestamp(calendar.time)

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val dateEnd = Timestamp(calendar.time)

            repository.getTransByMonth(dateStart,dateEnd,
                onSuccess = { transactions ->
                val transactionDetail = transactions.map { trans ->
                    val matchedCategory = listCategory.find { it.idCat == trans.categoryId }
                    trans.toDetail(matchedCategory)
                }
                    _transactionsDetail.value = transactionDetail
                    allTransactions = transactionDetail.toMutableList()
            },onFailure)
        }catch (e: ParseException){
            Log.e("ViewModel", "Sai định dạng tháng (MM/yyyy)")
        }

    }
    fun delTrans(idTrans: String,onSuccess:()->Unit,onFailure: (String) -> Unit){
        repository.delTrans(idTrans,onSuccess,onFailure)
    }
    fun filterTrans(query:String){
        if (query.isBlank()){
            _transactionsDetail.value = allTransactions
        }else{
            val result = allTransactions.filter {
                it.date.lowercase().contains(query.lowercase()) ||
                it.desCat?.lowercase()?.contains(query.lowercase()) ?: false ||
                it.amount.toString().contains(query)||
                it.note.lowercase().contains(query.lowercase())
            }
            _transactionsDetail.value = result
        }
    }



}

fun Transaction.toDetail(category: Category?): TransactionDetail {
    return TransactionDetail(
        idTrans = idTrans,
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