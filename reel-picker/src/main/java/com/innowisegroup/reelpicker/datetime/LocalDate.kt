package com.innowisegroup.reelpicker.datetime

import com.innowisegroup.reelpicker.extension.formattedMonth
import com.innowisegroup.reelpicker.extension.isWithinMinMaxRange
import java.io.Serializable
import java.util.*

class LocalDate private constructor(val day: Int, val month: Int, val year: Int) : Serializable {
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

    internal fun lengthOfMonth(): Int =
        when (month) {
            2 -> if (isLeapYear(year.toLong())) 29 else 28
            3, 5, 7, 8, 10 -> 31
            4, 6, 9, 11 -> 30
            else -> 31
        }

    internal fun withDayOfMonth(day: Int): LocalDate =
        if (this.day == day) this else of(day, month, year)

    internal fun withMonth(month: Int): LocalDate =
        if (this.month == month) this else resolvePreviousValid(day, month, year)

    internal fun withYear(year: Int): LocalDate =
        if (this.year == year) this else resolvePreviousValid(day, month, year)

    internal fun plusYears(yearsToAdd: Int): LocalDate =
        if (yearsToAdd == 0) {
            this
        } else {
            val newYear = year + yearsToAdd
            resolvePreviousValid(day, month, newYear)
        }

    override fun equals(other: Any?): Boolean =
        this.year == (other as LocalDate).year && this.month == other.month && this.day == other.day

    override fun hashCode(): Int = 31 * day + month + year

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
            validateDay(day)
            validateMonth(month)
            validateYear(year)
            return create(day, arrayMonth[month - 1], year)
        }

        fun of(day: Int, month: Month, year: Int): LocalDate {
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
        ): Boolean = if (date.year == minDate.year && date.year == maxDate.year) {
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

        private fun validateDay(day: Int) {
            require(day in MIN_DAY..MAX_DAY) { "Invalid days value" }
        }

        private fun validateMonth(month: Int) {
            require(month in MIN_MONTH..MAX_MONTH) { "Invalid months value" }
        }

        private fun validateYear(year: Int) {
            require(year in MIN_YEAR..MAX_YEAR) { "Invalid years value" }
        }

        private fun isLeapYear(currentYear: Long): Boolean =
            currentYear and 3L == 0L && (currentYear % 100L != 0L || currentYear % 400L == 0L)
    }
}