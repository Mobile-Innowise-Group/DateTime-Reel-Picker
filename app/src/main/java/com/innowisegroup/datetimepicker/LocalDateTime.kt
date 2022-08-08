package com.innowisegroup.datetimepicker

import java.io.Serializable

class LocalDateTime(private val date: LocalDate, private val time: LocalTime) : Serializable {

    fun toLocalDate(): LocalDate = date

    fun toLocalTime(): LocalTime = time

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
    }
}