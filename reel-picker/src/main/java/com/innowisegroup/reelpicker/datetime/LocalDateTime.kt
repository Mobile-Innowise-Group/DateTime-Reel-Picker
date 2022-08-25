package com.innowisegroup.reelpicker.datetime

import java.io.Serializable

class LocalDateTime private constructor(private val date: LocalDate, private val time: LocalTime) :
    Serializable {

    override fun equals(other: Any?): Boolean =
        this.date == (other as? LocalDateTime)?.toLocalDate() && this.time == other.toLocalTime()

    override fun hashCode(): Int = 31 * date.hashCode() + time.hashCode()

    fun toLocalDate(): LocalDate = date

    fun toLocalTime(): LocalTime = time

    companion object {
        @JvmStatic
        fun now(): LocalDateTime = LocalDateTime(LocalDate.now(), LocalTime.now())

        @JvmStatic
        fun of(day: Int, month: Month, year: Int, hour: Int, minute: Int): LocalDateTime =
            LocalDateTime(LocalDate.of(day, month, year), LocalTime.of(hour, minute))

        @JvmStatic
        fun of(day: Int, month: Int, year: Int, hour: Int, minute: Int): LocalDateTime =
            LocalDateTime(LocalDate.of(day, month - 1, year), LocalTime.of(hour, minute))

        @JvmStatic
        fun of(date: LocalDate, time: LocalTime): LocalDateTime = LocalDateTime(date, time)

        @JvmStatic
        fun of(date: LocalDate): LocalDateTime = LocalDateTime(date, LocalTime.now())

        @JvmStatic
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
        ) = require(
            initialLocalDate.getDaysOfDate() >= minLocalDate.getDaysOfDate() &&
                    initialLocalDate.getDaysOfDate() <= maxLocalDate.getDaysOfDate()
        ) { "initialLocalDate does not fit min..max range of minLocalDate and maxLocalDate" }

        private fun checkValidTime(
            initialLocalTime: LocalTime,
            minLocalTime: LocalTime,
            maxLocalTime: LocalTime
        ) = require(
            initialLocalTime.getSecondsOfTime() >= minLocalTime.getSecondsOfTime() &&
                    initialLocalTime.getSecondsOfTime() <= maxLocalTime.getSecondsOfTime()
        ) { "initialLocalTime does not fit min..max range of minLocalTime and maxLocalTime" }
    }
}