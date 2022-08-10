package com.innowisegroup.datetimepicker

import java.io.Serializable
import java.util.*

class LocalDate(day: Int, month: Int, year: Int) : Serializable {

    var day = day
        set(value) {
            validateDay(day)
            field = value
        }

    var month = month
        set(value) {
            validateMonth(month)
            field = value
        }

    var year = year
        set(value) {
            validateYear(year)
            field = value
        }

    init {
        this.day = day
        this.month = month
        this.year = year
    }

    private fun resolvePreviousValid(day: Int, month: Int, year: Int): LocalDate {
        var day2 = day
        when (month) {
            2 -> day2 = day2.coerceAtMost(if (isLeapYear(year.toLong())) 29 else 28)
            3, 5, 7, 8, 10 -> {}
            4, 6, 9, 11 -> day2 = day2.coerceAtMost(30)
            else -> {}
        }
        return of(day2, month, year)
    }

    fun getMinYear() = MIN_YEAR

    fun getMaxYear() = requireNonNull(year)

    @JvmName("getDayKotlin")
    fun getDay() = requireNonNull(day)

    @JvmName("getMonthKotlin")
    fun getMonth() = requireNonNull(month)

    @JvmName("getYearKotlin")
    fun getYear() = requireNonNull(year)

    fun lengthOfMonth(): Int =
        when (month) {
            2 -> if (isLeapYear(getYear().toLong())) 29 else 28
            3, 5, 7, 8, 10 -> 31
            4, 6, 9, 11 -> 30
            else -> 31
        }

    fun withDayOfMonth(day: Int): LocalDate =
        if (this.day == day) this else of(day, getMonth(), getYear())

    fun withMonth(month: Int): LocalDate =
        if (this.month == month) this else resolvePreviousValid(getDay(), month, getYear())

    fun withYear(year: Int): LocalDate =
        if (this.year == year) this else resolvePreviousValid(getDay(), getMonth(), year)

    fun plusYears(yearsToAdd: Int): LocalDate =
        if (yearsToAdd == 0) {
            this
        } else {
            val newYear = getYear() + yearsToAdd
            resolvePreviousValid(getDay(), getMonth(), newYear)
        }

    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        private val arrayMonth: Array<Month> = Month.values()

        private const val MIN_DAY = 1
        private const val MAX_DAY = 31
        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12
        private const val MIN_YEAR = 1900
        private const val MAX_YEAR = 2100

        fun now(): LocalDate {
            val getDay = calendar.get(Calendar.DAY_OF_MONTH)
            val getMonth = calendar.get(Calendar.MONTH).formattedMonth()
            val getYear = calendar.get(Calendar.YEAR)
            return LocalDate(getDay, getMonth, getYear)
        }

        fun of(day: Int, month: Int, year: Int): LocalDate {
            requireNonNull(day)
            requireNonNull(month)
            requireNonNull(year)
            validateDay(day)
            validateMonth(month)
            validateYear(year)
            return create(day, arrayMonth[month - 1], year)
        }

        fun of(day: Int, month: Month, year: Int): LocalDate {
            requireNonNull(day)
            requireNonNull(month)
            requireNonNull(year)
            validateDay(day)
            validateYear(year)
            return create(day, month, year)
        }

        private fun create(day: Int, month: Month, year: Int): LocalDate =
            if (day > 28 && day > month.length(isLeapYear(year.toLong()))) {
                if (day == 29) {
                    throw IllegalArgumentException("Invalid date 'February 29' as '$year' is not a leap year")
                } else {
                    throw IllegalArgumentException("Invalid date '" + month.name + " " + day + "'")
                }
            } else {
                LocalDate(day, month.of(), year)
            }

        internal fun isDateWithinMinMaxValue(
            date: LocalDate,
            minDate: LocalDate,
            maxDate: LocalDate
        ): Boolean {
            return if (date.year == minDate.year && date.year == maxDate.year) {
                isWithinMinMaxRange(
                    date.month,
                    minDate.month,
                    maxDate.month
                ) && isWithinMinMaxRange(
                    date.day,
                    minDate.day,
                    maxDate.day
                )
            } else {
                isWithinMinMaxRange(date.year, minDate.year, maxDate.year)
            }
        }

        private fun validateDay(day: Int) {
            if (day < MIN_DAY || day > MAX_DAY) throw IllegalArgumentException("Invalid days value")
        }

        private fun validateMonth(month: Int) {
            if (month < MIN_MONTH || month > MAX_MONTH) throw IllegalArgumentException("Invalid months value")
        }

        private fun validateYear(year: Int) {
            if (year < MIN_YEAR || year > MAX_YEAR) throw IllegalArgumentException("Invalid years value")
        }
    }
}