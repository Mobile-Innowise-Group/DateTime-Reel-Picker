package com.innowisegroup.reelpicker.datetime

import com.innowisegroup.reelpicker.extension.formattedMonth
import com.innowisegroup.reelpicker.extension.isLeapYear
import com.innowisegroup.reelpicker.extension.requireNonNull
import java.io.Serializable
import java.util.*

class LocalDate(val day: Int, val month: Int, val year: Int) : Serializable {

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

    fun getMinYear() = DEFAULT_MIN_YEAR

    fun getMaxYear() = requireNonNull(year)

    @JvmName("getDayKotlin")
    fun getDay() = requireNonNull(day)

    @JvmName("getMonthKotlin")
    fun getMonth() = requireNonNull(month)

    @JvmName("getYearKotlin")
    fun getYear() = requireNonNull(year)

    internal fun lengthOfMonth(): Int =
        when (month) {
            2 -> if (isLeapYear(getYear().toLong())) 29 else 28
            3, 5, 7, 8, 10 -> 31
            4, 6, 9, 11 -> 30
            else -> 31
        }

    internal fun withDayOfMonth(day: Int): LocalDate =
        if (this.day == day) this else of(day, getMonth(), getYear())

    internal fun withMonth(month: Int): LocalDate =
        if (this.month == month) this else resolvePreviousValid(getDay(), month, getYear())

    internal fun withYear(year: Int): LocalDate =
        if (this.year == year) this else resolvePreviousValid(getDay(), getMonth(), year)

    internal fun plusYears(yearsToAdd: Int): LocalDate =
        if (yearsToAdd == 0) {
            this
        } else {
            val newYear = getYear() + yearsToAdd
            resolvePreviousValid(getDay(), getMonth(), newYear)
        }

    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        private val arrayMonth: Array<Month> = Month.values()
        private const val DEFAULT_MIN_YEAR = 1900

        fun of(day: Int, month: Int, year: Int): LocalDate {
            requireNonNull(day)
            requireNonNull(month)
            requireNonNull(year)
            return create(day, arrayMonth[month - 1], year)
        }

        fun now(): LocalDate {
            val getDay = calendar.get(Calendar.DAY_OF_MONTH)
            val getMonth = calendar.get(Calendar.MONTH).formattedMonth()
            val getYear = calendar.get(Calendar.YEAR)
            return LocalDate(getDay, getMonth, getYear)
        }

        fun of(day: Int, month: Month, year: Int): LocalDate {
            requireNonNull(day)
            requireNonNull(month)
            requireNonNull(year)
            return create(day, month, year)
        }

        private fun create(day: Int, month: Month, year: Int): LocalDate =
            if (day > 28 && day > month.length(isLeapYear(year.toLong()))) {
                if (day == 29) {
                    throw Exception("Invalid date 'February 29' as '$year' is not a leap year")
                } else {
                    throw Exception("Invalid date '" + month.name + " " + day + "'")
                }
            } else {
                LocalDate(day, month.of(), year)
            }
    }
}