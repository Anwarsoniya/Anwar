package com.example.ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BanglaUtils {

    private val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBanglaDigits(input: String): String {
        val sb = StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(banglaDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun toBanglaDigits(number: Long): String = toBanglaDigits(number.toString())
    fun toBanglaDigits(number: Int): String = toBanglaDigits(number.toString())

    fun formatNumber(number: Double): String {
        val isWhole = (number % 1.0) == 0.0
        val pattern = if (isWhole) "#,##0" else "#,##0.#"
        val df = DecimalFormat(pattern)
        return toBanglaDigits(df.format(number))
    }

    fun formatTaka(amount: Double): String {
        return "৳" + formatNumber(amount)
    }

    fun getCurrentDateStr(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return toBanglaDigits(sdf.format(Date()))
    }

    fun getCurrentTimeStr(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return toBanglaDigits(sdf.format(Date()))
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return toBanglaDigits(sdf.format(Date(timestamp)))
    }

    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        return toBanglaDigits(sdf.format(Date(timestamp)))
    }
}
