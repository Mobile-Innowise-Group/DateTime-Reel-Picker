package com.innowisegroup.reelpicker.datetime

import androidx.annotation.IntRange
import com.innowisegroup.reelpicker.extension.MAX_HOUR
import com.innowisegroup.reelpicker.extension.MAX_MINUTE
import com.innowisegroup.reelpicker.extension.MIN_HOUR
import com.innowisegroup.reelpicker.extension.MIN_MINUTE
import java.io.Serializable
import java.util.*

class LocalTime private constructor(val hour: Int, val minute: Int) : Serializable {

    override fun equals(other: Any?): Boolean =
        this.hour == (other as? LocalTime)?.hour && this.minute == other.minute

    override fun hashCode(): Int = 31 * hour + minute

    fun plusHours(hoursToAdd: Int): LocalTime =
        if (hoursToAdd == 0) {
            this
        } else {
            val newHour = hour + hoursToAdd
            of(newHour, minute)
        }

    fun minusHours(hoursToSubtract: Int): LocalTime = plusHours(-hoursToSubtract)

    fun plusMinutes(minutesToAdd: Int): LocalTime =
        if (minutesToAdd == 0) {
            this
        } else {
            val minutesAfterAdding = hour * MINUTES_PER_HOUR + minute + minutesToAdd
            val newHour = minutesAfterAdding.floorDiv(MINUTES_PER_HOUR)
            val newMinute = minutesAfterAdding.mod(MINUTES_PER_HOUR)
            of(newHour, newMinute)
        }

    fun minusMinutes(minutesToSubtract: Int): LocalTime = plusMinutes(-minutesToSubtract)

    internal fun withHour(hour: Int): LocalTime =
        if (this.hour == hour) this else of(hour, this.minute)

    internal fun withMinute(minute: Int): LocalTime =
        if (this.minute == minute) this else of(this.hour, minute)

    internal fun getSecondsOfTime(): Int = hour * 3600 + minute * MINUTES_PER_HOUR

    companion object {
        private val calendar: Calendar = Calendar.getInstance()

        @JvmStatic
        fun now(): LocalTime {
            val getHour = calendar.get(Calendar.HOUR_OF_DAY)
            val getMinute = calendar.get(Calendar.MINUTE)
            return LocalTime(getHour, getMinute)
        }

        @JvmStatic
        fun of(
            @IntRange(from = MIN_HOUR.toLong(), to = MAX_HOUR.toLong()) hour: Int,
            @IntRange(from = MIN_MINUTE.toLong(), to = MAX_MINUTE.toLong()) minute: Int
        ): LocalTime {
            require(hour in MIN_HOUR..MAX_HOUR) { "Invalid hours value" }
            require(minute in MIN_MINUTE..MAX_MINUTE) { "Invalid minutes value" }
            return LocalTime(hour, minute)
        }
    }

    private val MINUTES_PER_HOUR = 60
}