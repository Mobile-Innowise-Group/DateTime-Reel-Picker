package com.innowisegroup.reel_picker.date_time

import com.innowisegroup.reel_picker.utils.requireNonNull
import java.io.Serializable
import java.util.*

class LocalTime(val hour: Int, val minute: Int) : Serializable {

    @JvmName("getHourKotlin")
    fun getHour() = requireNonNull(hour)

    @JvmName("getMinuteKotlin")
    fun getMinute() = requireNonNull(minute)

    internal fun withHour(hour: Int): LocalTime =
        if (this.hour == hour) this else create(hour, getMinute())

    internal fun withMinute(minute: Int): LocalTime =
        if (this.minute == minute) this else create(getHour(), minute)

    companion object {
        private const val MIN_HOUR = 0
        private const val MAX_HOUR = 23
        private const val MIN_MINUTE = 0
        private const val MAX_MINUTE = 59

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
    }
}