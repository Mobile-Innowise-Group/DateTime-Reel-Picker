package com.innowisegroup.reelpicker.datetime

import java.io.Serializable

class LocalDateTime private constructor(private val date: LocalDate, private val time: LocalTime) :
    Serializable {

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
            initialLocalDateTime: LocalDateTime,
            minLocalDateTime: LocalDateTime,
            maxLocalDateTime: LocalDateTime,
        ) {
            checkValidDate(
                initialLocalDateTime.toLocalDate(),
                minLocalDateTime.toLocalDate(),
                maxLocalDateTime.toLocalDate()
            )
            checkValidTime(
                initialLocalDateTime.toLocalTime(),
                minLocalDateTime.toLocalTime(),
                maxLocalDateTime.toLocalTime()
            )
        }

        private fun checkValidDate(
            initialLocalDate: LocalDate,
            minLocalDate: LocalDate,
            maxLocalDate: LocalDate
        ) {
            require(initialLocalDate.year >= minLocalDate.year)
            { "minLocalDate must not be more than initialLocalDate" }
            require((initialLocalDate.month >= minLocalDate.month) || (initialLocalDate.year != minLocalDate.year))
            { "minLocalDate must not be more than initialLocalDate" }
            require((initialLocalDate.day >= minLocalDate.day) || (initialLocalDate.month != minLocalDate.month) || (initialLocalDate.year != minLocalDate.year))
            { "minLocalDate must not be more than initialLocalDate" }

            require(initialLocalDate.year <= maxLocalDate.year)
            { "maxLocalDate must not be less than initialLocalDate" }
            require((initialLocalDate.month <= maxLocalDate.month) || (initialLocalDate.year != maxLocalDate.year))
            { "maxLocalDate must not be less than initialLocalDate" }
            require(((initialLocalDate.day <= maxLocalDate.day) || (initialLocalDate.month != maxLocalDate.month) || (initialLocalDate.year != minLocalDate.year)))
            { "maxLocalDate must not be less than initialLocalDate" }
        }

        private fun checkValidTime(
            initialLocalTime: LocalTime,
            minLocalTime: LocalTime,
            maxLocalTime: LocalTime
        ) {
            require(initialLocalTime.hour >= minLocalTime.hour)
            { "minLocalTime must not be more than initialLocalTime" }
            require((initialLocalTime.minute >= minLocalTime.minute) || (initialLocalTime.hour != minLocalTime.hour))
            { "minLocalTime must not be more than initialLocalTime" }

            require((initialLocalTime.hour <= maxLocalTime.hour))
            { "maxLocalTime must not be less than initialLocalTime" }
            require((initialLocalTime.minute <= maxLocalTime.minute) || (initialLocalTime.hour != maxLocalTime.hour))
            { "maxLocalTime must not be less than initialLocalTime" }
        }
    }
}