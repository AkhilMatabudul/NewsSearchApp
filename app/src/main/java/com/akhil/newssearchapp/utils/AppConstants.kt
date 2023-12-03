package com.akhil.newssearchapp.utils

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object AppConstants {

    // hide keyboard function
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //date converter to human readable formate
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

        val dateTime = LocalDateTime.parse(inputDate, inputFormatter)
        return dateTime.format(outputFormatter)
    }

}