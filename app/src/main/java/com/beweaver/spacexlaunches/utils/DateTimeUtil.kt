package com.beweaver.spacexlaunches.utils

import java.text.SimpleDateFormat
import java.util.Locale

class DateTimeUtil {
    companion object {
        fun convertTimestampToReadable(timestamp: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())

            try {
                val date = inputFormat.parse(timestamp)
                return outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }
    }
}