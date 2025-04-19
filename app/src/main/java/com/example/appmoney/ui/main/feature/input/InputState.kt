package com.example.appmoney.ui.main.feature.input

import com.example.appmoney.data.model.Category
import java.util.Calendar

data class InputState (

    val date: Calendar = Calendar.getInstance(),
    val note: String = "",
    val amount: Long = 0,
    val isUpdate: Boolean = false,
)