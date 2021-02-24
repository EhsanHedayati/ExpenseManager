package com.mylab.expensemanager.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.chrono.PersianChronologyKhayyam

object Utils {


    fun getPersianDate(milli: Long): String {
        val perChr = PersianChronologyKhayyam.getInstance(DateTimeZone.forID("UTC"))
        val dateTime = DateTime(milli, perChr)
        return dateTime.toString("yyyy / MM / dd")
    }


}