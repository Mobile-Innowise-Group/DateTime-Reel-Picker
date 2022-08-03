package com.innowisegroup.datetimepicker

import java.io.Serializable

class LocalDateTime() : Serializable {

    constructor(date: LocalDate, time: LocalTime) : this() {
        this.date = date
        this.time = time
    }

    private var date: LocalDate? = null
    private var time: LocalTime? = null

    fun now(): LocalDateTime {
        val date = LocalDate().now()
        val time = LocalTime().now()
        return LocalDateTime(date, time)
    }

    fun of(day: Int, month: Month, year: Int, hour: Int, minute: Int): LocalDateTime {
        val date = LocalDate().of(day, month, year)
        val time = LocalTime().of(hour, minute)
        return LocalDateTime(date, time)
    }

    fun of(day: Int, month: Int, year: Int, hour: Int, minute: Int): LocalDateTime {
        requireNonNull(day)
        requireNonNull(month)
        requireNonNull(year)
        requireNonNull(hour)
        requireNonNull(minute)
        val date = LocalDate().of(day, month - 1, year)
        val time = LocalTime().of(hour, minute)
        return LocalDateTime(date, time)
    }

    fun of(date: LocalDate, time: LocalTime): LocalDateTime {
        requireNonNull(date)
        requireNonNull(time)
        return LocalDateTime(date, time)
    }

    fun toLocalDate(): LocalDate? = this.date

    fun toLocalTime(): LocalTime? = this.time
}