package com.innowisegroup.datetimepicker

import java.io.Serializable
import java.util.*

class LocalTime() : Serializable {
    constructor(hour: Int, minute: Int) : this() {
        this.hour = hour
        this.minute = minute
    }

    private var hour: Int? = null
    private var minute: Int? = null

    private val calendar: Calendar = Calendar.getInstance()

    fun now(): LocalTime {
        val getHour = calendar.get(Calendar.HOUR_OF_DAY)
        val getMinute = calendar.get(Calendar.MINUTE)
        return LocalTime(getHour, getMinute)
    }

    fun of(hour: Int, minute: Int): LocalTime {
        requireNonNull(hour)
        requireNonNull(minute)
        return create(hour, minute)
    }

    private fun create(hour: Int, minute: Int): LocalTime {
        timeChecker(hour, minute)
        return LocalTime(hour, minute)
    }

    private fun timeChecker(hour: Int, minute: Int) {
        if (hour > MAX_HOUR || hour < MIN_HOUR)
            throw Exception("Incorrect hour")
        if (minute < MIN_MINUTE || minute > MAX_MINUTE)
            throw Exception("Incorrect minutes")
    }

    fun getHour() = requireNonNull(this.hour)

    fun getMinute() = requireNonNull(this.minute)

    fun withHour(hour: Int): LocalTime =
            if (this.hour == hour) this else create(hour, getMinute())

    fun withMinute(minute: Int): LocalTime =
            if (this.minute == minute) this else create(getHour(), minute)

    private companion object {
        const val MIN_HOUR = 0
        const val MAX_HOUR = 23
        const val MIN_MINUTE = 0
        const val MAX_MINUTE = 59
    }
}