package com.example.appmoney.ui.main.feature.input

import java.util.Calendar

data class TransactionState (
    val idTrans: String? = null,
    val date: Calendar = Calendar.getInstance(),
    val note: String = "",
    val amount: Long = 0,
)