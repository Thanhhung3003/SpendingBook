package com.example.appmoney.ui.common.helper

import android.util.Log
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeHelper {

    private val calendar = Calendar.getInstance()
    private val locale = Locale.getDefault()

    fun updateTime(time: Long) {
        calendar.timeInMillis = time
    }

    fun getByFormat(cal: Calendar, format: TimeFormat): String {
        return try {
                SimpleDateFormat(format.formatString, locale).format(cal.time)
        } catch (e: IllegalArgumentException) {
            Log.e(this.javaClass.name, "err: $e")
            ""
        }
    }

    fun fromString(date: String, formatString: String) {
        try {
            SimpleDateFormat(formatString, locale).parse(date)?.time?.let {
                calendar.timeInMillis = it
            }
        } catch (e: ParseException) {
            Log.e(this.javaClass.name, "err: $e")
        }
    }
    fun stringToTimestamp( dateString: String): Timestamp?{
        return try {
            val sfd = SimpleDateFormat(TimeFormat.Date.formatString, locale)
            val date = sfd.parse(dateString)
            if (date!= null) Timestamp(date) else null
        }catch (e : Exception){
            Log.e(this.javaClass.name, "err: $e")
            null
        }
    }
    fun timestampToString( timestamp: Timestamp): String{
        return try {
            val sfd = SimpleDateFormat(TimeFormat.Date.formatString, locale)
            sfd.format(timestamp.toDate())
        }catch (e: Exception){
            Log.e(this.javaClass.name,"err: $e")
            ""
        }
    }
}

enum class TimeFormat(val formatString: String) {
    Time("hh:mm:ss"),
    Date("EEE,dd/MM/yyyy"),
    MonthDate("MM/yyyy"),
    DateTime("dd/MM/yyyy hh:mm:ss")

}