package com.innowisegroup.reelpicker.datetime

import com.innowisegroup.reelpicker.datetime.LocalDate.Companion.isDateWithinMinMaxValue
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MAX_HOUR
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MAX_MINUTE
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MIN_HOUR
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.MIN_MINUTE
import com.innowisegroup.reelpicker.datetime.LocalTime.Companion.isTimeWithinMinMaxValue
import java.io.Serializable

class LocalDateTime(private val date: LocalDate, private val time: LocalTime) : Serializable {

    fun toLocalDate(): LocalDate = date

    fun toLocalTime(): LocalTime = time

    override fun equals(other: Any?): Boolean =
        this.date == (other as? LocalDateTime)?.toLocalDate() && this.time == other.toLocalTime()

    override fun hashCode(): Int = 31 * date.hashCode() + time.hashCode()

    companion object {
        fun now(): LocalDateTime = LocalDateTime(LocalDate.now(), LocalTime.now())

        fun of(day: Int, month: Month, year: Int, hour: Int, minute: Int): LocalDateTime =
            LocalDateTime(LocalDate.of(day, month, year), LocalTime.of(hour, minute))

        fun of(day: Int, month: Int, year: Int, hour: Int, minute: Int): LocalDateTime =
            LocalDateTime(LocalDate.of(day, month - 1, year), LocalTime.of(hour, minute))

        fun of(date: LocalDate, time: LocalTime): LocalDateTime = LocalDateTime(date, time)

        fun of(date: LocalDate): LocalDateTime = LocalDateTime(date, LocalTime.now())

        fun of(time: LocalTime): LocalDateTime = LocalDateTime(LocalDate.now(), time)

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
                        LocalTime.of(MAX_HOUR, MAX_MINUTE)
                    )
                ) throw IllegalArgumentException(
                    "Initial time value must be within max and min bounds"
                )
            }
            if (initialLocalTime == maxLocalDateTime) {
                if (!isTimeWithinMinMaxValue(
                        initialLocalTime.toLocalTime(),
                        LocalTime.of(MIN_HOUR, MIN_MINUTE),
                        maxLocalDateTime.toLocalTime()
                    )
                ) throw IllegalArgumentException(
                    "Initial time value must be within max and min bounds"
                )
            }
        }
    }
}