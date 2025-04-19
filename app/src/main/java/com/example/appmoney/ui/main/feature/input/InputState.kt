package com.example.appmoney.ui.main.feature.input

import java.util.Calendar

data class InputState (
    val date: Calendar = Calendar.getInstance(),
    val note: String = "",
    val amount: String = "",
    val category: String = "",
    val type: String = "",
)