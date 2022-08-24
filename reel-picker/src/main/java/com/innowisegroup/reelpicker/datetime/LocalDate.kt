package com.innowisegroup.reelpicker.datetime

import androidx.annotation.IntRange
import com.innowisegroup.reelpicker.datetime.Month.Companion.of
import com.innowisegroup.reelpicker.extension.*
import java.io.Serializable
import java.util.*

class LocalDate private constructor(val day: Int, val month: Int, val year: Int) : Serializable {

    private val DAYS_PER_CYCLE = 146097

    private val DAYS_0000_TO_1970 = DAYS_PER_CYCLE * 5 - (30 * 365 + 7)

    override fun equals(other: Any?): Boolean =
        this.year == (other as LocalDate).year && this.month == other.month && this.day == other.day

    override fun hashCode(): Int = 31 * day + month + year

    fun plusDays(daysToAdd: Int): LocalDate =
        if (daysToAdd == 0) {
            this
        } else {
            val mjDay = Math.addExact(toEpochDay(), daysToAdd)
            ofEpochDay(mjDay)
        }

    private fun toEpochDay(): Int {
        val y = year
        val m = month
        var total = 0
        total += 365 * y
        if (y >= 0) {
            total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400
        } else {
            total -= y / -4 - y / -100 + y / -400
        }
        total += (367 * m - 362) / 12
        total += (day - 1)
        if (m > 2) {
            total--
            if (!isLeapYear(year.toLong())) {
                total--
            }
        }
        return total - DAYS_0000_TO_1970
    }

    private fun ofEpochDay(epochDay: Int): LocalDate {
        var zeroDay = epochDay + DAYS_0000_TO_1970
        zeroDay -= 60
        var adjust = 0
        if (zeroDay < 0) {
            val adjustCycles = (zeroDay + 1) / DAYS_PER_CYCLE - 1
            adjust = adjustCycles * 400
            zeroDay += -adjustCycles * DAYS_PER_CYCLE
        }
        var yearEst = (400 * zeroDay + 591) / DAYS_PER_CYCLE
        var doyEst = zeroDay - (365 * yearEst + yearEst / 4 - yearEst / 100 + yearEst / 400)
        if (doyEst < 0) {
            yearEst--
            doyEst = zeroDay - (365 * yearEst + yearEst / 4 - yearEst / 100 + yearEst / 400)
        }
        yearEst += adjust
        val marchDoy0 = doyEst

        val marchMonth0 = (marchDoy0 * 5 + 2) / 153
        val month = (marchMonth0 + 2) % 12 + 1
        val dom = marchDoy0 - (marchMonth0 * 306 + 5) / 10 + 1
        yearEst += (marchMonth0 / 10)

        val year = yearEst
        return of(dom, month, year)
    }

    fun minusDays(daysToMinus: Int): LocalDate =
        if (daysToMinus == 0) {
            this
        } else {
            val newDay = day - daysToMinus
            resolvePreviousValid(newDay, month, year)
        }

    fun plusMonths(monthsToAdd: Int): LocalDate =
        if (monthsToAdd == 0) {
            this
        } else {
            val monthCount = year * 12L + (month - 1)
            val calcMonths: Long = monthCount + monthsToAdd
            val newYear = Math.floorDiv(calcMonths, 12).toInt()
            val newMonth = Math.floorMod(calcMonths, 12) + 1
            resolvePreviousValid(day, newMonth, newYear)
        }

    fun minusMonths(monthsToSubtract: Int): LocalDate =
        if (monthsToSubtract.toLong() == Long.MIN_VALUE)
            plusMonths(Long.MAX_VALUE.toInt()).plusMonths(1)
        else plusMonths(-monthsToSubtract)

    fun plusYears(yearsToAdd: Int): LocalDate =
        if (yearsToAdd == 0) {
            this
        } else {
            val newYear = year + yearsToAdd
            resolvePreviousValid(day, month, newYear)
        }

    fun minusYears(yearsToMinus: Int): LocalDate = plusYears(-yearsToMinus)

    internal fun withDayOfMonth(day: Int): LocalDate =
        if (this.day == day) this else of(day, month, year)

    internal fun withMonth(month: Int): LocalDate =
        if (this.month == month) this else resolvePreviousValid(day, month, year)

    internal fun withYear(year: Int): LocalDate =
        if (this.year == year) this else resolvePreviousValid(day, month, year)

    internal fun lengthOfMonth(): Int =
        when (month) {
            2 -> if (isLeapYear(year.toLong())) 29 else 28
            3, 5, 7, 8, 10 -> 31
            4, 6, 9, 11 -> 30
            else -> 31
        }

    internal fun getHoursOfDate(): Int =
        (year * if (isLeapYear(year.toLong())) 8784 else 8760) + (month * lengthOfMonth() * 24) + day * 24

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

    companion object {
        private val calendar: Calendar = Calendar.getInstance()

        @JvmStatic
        fun now(): LocalDate {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            return LocalDate(day, month, year)
        }

        @JvmStatic
        fun of(
            @IntRange(from = MIN_DAY.toLong(), to = MAX_DAY_DEFAULT.toLong()) day: Int,
            @IntRange(from = MIN_MONTH.toLong(), to = MAX_MONTH.toLong()) month: Int,
            @IntRange(from = MIN_YEAR.toLong(), to = MAX_YEAR.toLong()) year: Int
        ): LocalDate {
            validateDay(day, month.of(), year)
            validateMonth(month)
            validateYear(year)
            return LocalDate(day, month, year)
        }

        @JvmStatic
        fun of(
            @IntRange(from = MIN_DAY.toLong(), to = MAX_DAY_DEFAULT.toLong()) day: Int,
            month: Month,
            @IntRange(from = MIN_YEAR.toLong(), to = MAX_YEAR.toLong()) year: Int
        ): LocalDate {
            validateDay(day, month, year)
            validateYear(year)
            return LocalDate(day, month.of(), year)
        }

        private fun validateDay(day: Int, month: Month, year: Int) =
            require(day in MIN_DAY..month.length(isLeapYear(year.toLong()))) { "Invalid date ${month.name} $day" }

        private fun validateMonth(month: Int) =
            require(month in MIN_MONTH..MAX_MONTH) { "Invalid month value" }

        private fun validateYear(year: Int) =
            require(year in MIN_YEAR..MAX_YEAR) { "Invalid year value" }

        private fun isLeapYear(currentYear: Long): Boolean =
            currentYear and 3L == 0L && (currentYear % 100L != 0L || currentYear % 400L == 0L)
    }
}