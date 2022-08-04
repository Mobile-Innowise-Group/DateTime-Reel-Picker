package com.innowisegroup.datetimepicker

internal fun LocalTime.formatTime(): String {
    val hour = hour
    val correctHour = if (hour < 10) "0$hour" else "$hour"
    val minute = minute
    val correctMinute = if (minute < 10) "0$minute" else "$minute"
    return "$correctHour:$correctMinute"
}

internal fun LocalDate.formatDate(): String {
    val day = day
    val correctDay = if (day < 10) "0$day" else "$day"
    val month = month
    val correctMonth = if (month < 10) "0$month" else "$month"
    val year = year
    return "$correctDay.$correctMonth.$year"
}

internal fun Int.formattedMonth(): Int = this + 1