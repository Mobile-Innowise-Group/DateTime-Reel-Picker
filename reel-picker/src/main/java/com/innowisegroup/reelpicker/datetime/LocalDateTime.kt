package com.innowisegroup.reelpicker.datetime

import com.innowisegroup.reelpicker.datetime.LocalDate.Companion.isDateWithinMinMaxValue
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MAX_HOUR
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MAX_MINUTE
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MIN_HOUR
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MIN_MINUTE
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.isTimeWithinMinMaxValue
import com.innowisegroup.reelpicker.extension.requireNonNull
import java.io.Serializable

class LocalDateTime(private val date: LocalDate, private val time: LocalTime) : Serializable {

    fun toLocalDate(): LocalDate = date

    fun toLocalTime(): LocalTime = time

    override fun equals(other: Any?): Boolean =
        this.date == (other as? LocalDateTime)?.toLocalDate() && this.time == other.toLocalTime()

    override fun hashCode(): Int = 31 * date.hashCode() + time.hashCode()

    companion object {
        fun now(): LocalDateTime {
            val date = LocalDate.now()
            val time = LocalTime.now()
            return LocalDateTime(date, time)
        }

        fun of(day: Int, month: Month, year: Int, hour: Int, minute: Int): LocalDateTime {
            val date = LocalDate.of(day, month, year)
            val time = LocalTime.of(hour, minute)
            return LocalDateTime(date, time)
        }

        fun of(day: Int, month: Int, year: Int, hour: Int, minute: Int): LocalDateTime {
            requireNonNull(day)
            requireNonNull(month)
            requireNonNull(year)
            requireNonNull(hour)
            requireNonNull(minute)
            val date = LocalDate.of(day, month - 1, year)
            val time = LocalTime.of(hour, minute)
            return LocalDateTime(date, time)
        }

        fun of(date: LocalDate, time: LocalTime): LocalDateTime {
            requireNonNull(date)
            requireNonNull(time)
            return LocalDateTime(date, time)
        }

        fun of(date: LocalDate): LocalDateTime {
            requireNonNull(date)
            return LocalDateTime(date, LocalTime.now())
        }

        fun of(time: LocalTime): LocalDateTime {
            requireNonNull(time)
            return LocalDateTime(LocalDate.now(), time)
        }

        internal fun validateInputDateTime(
            initialLocalTime: LocalDateTime,
            minLocalDateTime: LocalDateTime,
            maxLocalDateTime: LocalDateTime,
        ) {
            if (!isDateWithinMinMaxValue(
                    initialLocalTime.toLocalDate(),
                    minLocalDateTime.toLocalDate(),
                    maxLocalDateTime.toLocalDate()
                )
            ) throw IllegalArgumentException(
                "Initial date value must be within max and min bounds"
            )
            if (initialLocalTime == minLocalDateTime) {
                if (!isTimeWithinMinMaxValue(
                        initialLocalTime.toLocalTime(),
                        minLocalDateTime.toLocalTime(),
                        LocalTime(MAX_HOUR, MAX_MINUTE)
                    )
                ) throw IllegalArgumentException(
                    "Initial time value must be within max and min bounds"
                )
            }
            if (initialLocalTime == maxLocalDateTime) {
                if (!isTimeWithinMinMaxValue(
                        initialLocalTime.toLocalTime(),
                        LocalTime(MIN_HOUR, MIN_MINUTE),
                        maxLocalDateTime.toLocalTime()
                    )
                ) throw IllegalArgumentException(
                    "Initial time value must be within max and min bounds"
                )
            }
        }
    }
}