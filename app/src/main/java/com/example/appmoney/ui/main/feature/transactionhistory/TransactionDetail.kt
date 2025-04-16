package com.example.appmoney.ui.main.feature.transactionhistory

import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage

data class TransactionDetail(
    val id :String = "",
    val amount: Long = 0,
    val date: String = "",
    val note: String = "",
    val categoryId : String= "",
    val typeTrans: String= "Income",
    val image: CategoryImage? = CategoryImage.BUS,
    val color: CategoryColor? = CategoryColor.BLACK,
    val desCat: String? = "",
)
