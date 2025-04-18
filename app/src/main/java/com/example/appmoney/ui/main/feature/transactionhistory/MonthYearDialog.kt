package com.example.appmoney.ui.main.feature.transactionhistory

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.appmoney.databinding.MonthYearDialogBinding
import java.util.Calendar

class MonthYearDialog(
    private val context: Context,
    private val ShowMonthYear: (moth: Int, year: Int) -> Unit
) {
    private val calendar = Calendar.getInstance()

    fun show(){
        val binding = MonthYearDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        val months = arrayOf(
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12")

        binding.apply {
            npMonth.minValue = 1
            npMonth.maxValue = 12
            npMonth.displayedValues = months
            npMonth.value = currentMonth

            npYear.minValue = 2000
            npYear.maxValue = 2100
            npYear.value = currentYear

            btnOk.setOnClickListener {
                val moth = npMonth.value
                val year = npYear.value
                ShowMonthYear(moth,year)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener { dialog.dismiss()}
        }
        dialog.show()
    }

}