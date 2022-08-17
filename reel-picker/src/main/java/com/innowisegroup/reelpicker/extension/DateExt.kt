package com.innowisegroup.reelpicker.extension

import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.LocalTime

internal fun LocalTime.formatTime(): String {
    val correctHour = if (hour < 10) "0$hour" else "$hour"
    val correctMinute = if (minute < 10) "0$minute" else "$minute"
    return "$correctHour:$correctMinute"
}

internal fun LocalDate.formatDate(): String {
    val correctDay = if (day < 10) "0$day" else "$day"
    val correctMonth = if (month < 10) "0$month" else "$month"
    return "$correctDay.$correctMonth.$year"
}

internal fun Int.formattedMonth(): Int = this + 1

internal fun isWithinMinMaxRange(value: Int, min: Int, max: Int) = value in min..max