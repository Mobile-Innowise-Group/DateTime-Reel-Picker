package com.innowisegroup.reelpicker.datetime

import com.innowisegroup.reelpicker.extension.requireNonNull
import java.io.Serializable
import java.util.*

class LocalTime(hour: Int, minute: Int) : Serializable {

    var hour = hour
        set(value) {
            validateHour(hour)
            field = value
        }

    var minute = minute
        set(value) {
            validateMinute(minute)
            field = value
        }

    init {
        this.hour = hour
        this.minute = minute
    }

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
            validateHour(hour)
            validateMinute(minute)
            return LocalTime(hour, minute)
        }

        internal fun isTimeWithinMinMaxValue(
            time: LocalTime,
            minTime: LocalTime,
            maxTime: LocalTime
        ) = isWithinMinMaxRange(
            time.hour,
            minTime.hour,
            maxTime.hour
        ) && isWithinMinMaxRange(
            time.minute,
            minTime.minute,
            maxTime.minute
        )

        private fun validateHour(hour: Int) {
            if (hour > MAX_HOUR || hour < MIN_HOUR) throw IllegalArgumentException("Invalid hours value")
        }

        private fun validateMinute(minute: Int) {
            if (minute < MIN_MINUTE || minute > MAX_MINUTE) throw IllegalArgumentException("Invalid minutes value")
        }
    }
}