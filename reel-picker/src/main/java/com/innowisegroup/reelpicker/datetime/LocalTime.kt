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

    internal fun withHour(hour: Int): LocalTime =
        if (this.hour == hour) this else of(hour, this.minute)

    internal fun withMinute(minute: Int): LocalTime =
        if (this.minute == minute) this else of(this.hour, minute)

    internal fun getSecondsOfTime(): Int = hour * 3600 + minute * 60

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
}