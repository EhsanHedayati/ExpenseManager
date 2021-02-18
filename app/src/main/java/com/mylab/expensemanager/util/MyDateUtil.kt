package com.mylab.expensemanager.util

import android.R.string
import android.annotation.SuppressLint
import android.util.Log
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.chrono.PersianChronologyKhayyam
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "MyDateUtil"

@SuppressLint("SimpleDateFormat")
fun getPersianDate(date: String): Long {
    val perChr = PersianChronologyKhayyam.getInstance(DateTimeZone.forID("UTC"))
    val formatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
        .withLocale(Locale.ROOT)
        .withChronology(perChr)
    val dt: DateTime = formatter.parseDateTime(date)
    Log.i(TAG, "getPersianDate1: ${dt.toString()}")
    return dt.millis
}

fun firsDayOfMonth(year: Int, month: Int): Long {
    val date = "$year-$month-1"
    return getPersianDate(date)
}

fun firsDayOfMonth(): Long {
    val dateTime: DateTime = getTodayDateTime()
    val date = "${dateTime.year}-${dateTime.monthOfYear}-1"
    Log.i(TAG, "firsDayOfMonth: $date")
    return getPersianDate(date)
}

fun firsDayOfYear(): Long {
    val dateTime: DateTime = getTodayDateTime()
    val date = "${dateTime.year}-1-1"
    Log.i(TAG, "firsDayOfMonth: $date")
    return getPersianDate(date)
}

fun firsDayOfWeek(): Long {
    val dateTime: DateTime = getTodayDateTime()
    val firstWeek = dateTime.minusDays(dateTime.dayOfWeek-1)
    return firstWeek.millis
}


fun getToday(): String {
    val perChr = PersianChronologyKhayyam.getInstance(DateTimeZone.forID("UTC"))
    val date = Date()
    val dateTime = DateTime(date.time, perChr)
    return dateTime.toString("yyyy-MM-dd")
}

fun getPersianDate(milli: Long): String {
    val perChr = PersianChronologyKhayyam.getInstance(DateTimeZone.forID("UTC"))
    val dateTime = DateTime(milli, perChr)
    return dateTime.toString("yyyy-MM-dd")
}


fun getTodayDateTime(): DateTime {
    val perChr = PersianChronologyKhayyam.getInstance(DateTimeZone.forID("UTC"))
    val date = Date()
    val dateTime = DateTime(date.time, perChr)
    return dateTime
}
