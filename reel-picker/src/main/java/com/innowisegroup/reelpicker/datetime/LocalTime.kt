package com.innowisegroup.reelpicker.datetime

import com.innowisegroup.reelpicker.extension.isWithinMinMaxRange
import java.io.Serializable
import java.util.*

class LocalTime private constructor(val hour: Int, val minute: Int) : Serializable {

    override fun equals(other: Any?): Boolean =
        this.hour == (other as? LocalTime)?.hour && this.minute == other.minute

    override fun hashCode(): Int = 31 * hour + minute

    internal fun withHour(hour: Int): LocalTime =
        if (this.hour == hour) this else of(hour, this.minute)

    internal fun withMinute(minute: Int): LocalTime =
        if (this.minute == minute) this else of(this.hour, minute)

    companion object {
        internal const val MIN_HOUR = 0
        internal const val MAX_HOUR = 23
        internal const val MIN_MINUTE = 0
        internal const val MAX_MINUTE = 59

        private val calendar: Calendar = Calendar.getInstance()

        fun now(): LocalTime {
            val getHour = calendar.get(Calendar.HOUR_OF_DAY)
            val getMinute = calendar.get(Calendar.MINUTE)
            return LocalTime(getHour, getMinute)
        }

        fun of(hour: Int, minute: Int): LocalTime {
            require(hour in MIN_HOUR..MAX_HOUR) { "Invalid hours value" }
            require(minute in MIN_MINUTE..MAX_MINUTE) { "Invalid minutes value" }
            return LocalTime(hour, minute)
        }

        internal fun isTimeWithinMinMaxValue(
            time: LocalTime,
            minTime: LocalTime,
            maxTime: LocalTime
        ): Boolean = if (time.hour == minTime.hour && time.hour == maxTime.hour) {
            isWithinMinMaxRange(time.minute, minTime.minute, maxTime.minute)
        } else {
            isWithinMinMaxRange(time.hour, minTime.hour, maxTime.hour)
                    && isWithinMinMaxRange(time.minute, minTime.minute, maxTime.minute)
        }
    }
}