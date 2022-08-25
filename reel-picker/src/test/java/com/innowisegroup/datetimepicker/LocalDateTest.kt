package com.innowisegroup.datetimepicker

import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.Month
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.*

class LocalDateTest {

    @Test
    fun `test plusDays() in case of this year`() {
        val dateExpected = LocalDate.of(25, 8, 2022)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2022).plusDays(236)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test plusDays() in case of different years`() {
        val dateExpected = LocalDate.of(1, 3, 2024)
        val dateActual: LocalDate = LocalDate.of(28, 2, 2022).plusDays(732)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test plusMonths() in case of this year`() {
        val dateExpected = LocalDate.of(1, 11, 2022)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2022).plusMonths(10)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test plusMonths() in case of different years`() {
        val dateExpected = LocalDate.of(28, 3, 2024)
        val dateActual: LocalDate = LocalDate.of(28, 2, 2022).plusMonths(25)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test plusYears() in case of not leap years`() {
        val dateExpected = LocalDate.of(1, 1, 2032)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2022).plusYears(10)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test plusYears() in case of leap year`() {
        val dateExpected = LocalDate.of(28, 2, 2025)
        val dateActual: LocalDate = LocalDate.of(29, 2, 2024).plusYears(1)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test minusDays() in case of this year`() {
        val dateExpected = LocalDate.of(14, 2, 2022)
        val dateActual: LocalDate = LocalDate.of(5, 3, 2022).minusDays(19)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test minusDays() in case of different years`() {
        val dateExpected = LocalDate.of(29, 2, 2020)
        val dateActual: LocalDate = LocalDate.of(28, 2, 2022).minusDays(730)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test minusMonths() in case of this year`() {
        val dateExpected = LocalDate.of(1, 12, 2021)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2022).minusMonths(1)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test minusMonths() in case of different years`() {
        val dateExpected = LocalDate.of(15, 1, 2019)
        val dateActual: LocalDate = LocalDate.of(15, 2, 2022).minusMonths(37)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test minusYears() in case of not leap years`() {
        val dateExpected = LocalDate.of(1, 1, 2010)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2022).minusYears(12)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test minusYears() in case of leap year`() {
        val dateExpected = LocalDate.of(29, 2, 2020)
        val dateActual: LocalDate = LocalDate.of(29, 2, 2024).minusYears(4)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test withDays() in case of current day`() {
        val dateExpected = LocalDate.of(25, 8, 2022)
        val dateActual: LocalDate = LocalDate.of(25, 8, 2022).withDayOfMonth(25)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test withMonths() in case of current month`() {
        val dateExpected = LocalDate.of(25, 8, 2022)
        val dateActual: LocalDate = LocalDate.of(25, 8, 2022).withMonth(8)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test withYears() in case of current year`() {
        val dateExpected = LocalDate.of(25, 8, 2022)
        val dateActual: LocalDate = LocalDate.of(25, 8, 2022).withYear(2022)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test withDays() in case of different days`() {
        val dateExpected = LocalDate.of(28, 2, 2001)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2001).withDayOfMonth(28).withMonth(2)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test withMonths() in case of different months`() {
        val dateExpected = LocalDate.of(1, 12, 2001)
        val dateActual: LocalDate = LocalDate.of(1, 1, 2001).withMonth(12)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test withYears() in case of different years`() {
        val dateExpected = LocalDate.of(28, 2, 1997)
        val dateActual: LocalDate = LocalDate.of(29, 2, 2024).withYear(1997)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test now()`() {
        val calendar: Calendar = Calendar.getInstance()
        val hoursValueExpected = LocalDate.of(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )
        val dateActual = LocalDate.now()

        Assert.assertEquals(hoursValueExpected, dateActual)
    }

    @Test
    fun `test of()`() {
        val dateExpected = LocalDate.of(29, 2, 2024)
        val dateActual: LocalDate = LocalDate.of(29, 2, 2024)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test of() for Month value`() {
        val dateExpected = LocalDate.of(29, 2, 2024)
        val dateActual: LocalDate = LocalDate.of(29, Month.FEBRUARY, 2024)

        Assert.assertEquals(dateExpected.day, dateActual.day)
        Assert.assertEquals(dateExpected.month, dateActual.month)
        Assert.assertEquals(dateExpected.year, dateActual.year)
    }

    @Test
    fun `test incorrect day in of() parameters`() {
        val illegalArgumentException =
            assertThrows(IllegalArgumentException::class.java) {
                LocalDate.of(29, Month.FEBRUARY, 2023)
            }
        assertThat(
            illegalArgumentException.message,
            CoreMatchers.containsString("Invalid date FEBRUARY 29")
        )
    }

    @Test
    fun `test incorrect month in of() parameters`() {
        val illegalArgumentException =
            assertThrows(IllegalArgumentException::class.java) {
                LocalDate.of(25, 13, 2023)
            }
        assertThat(
            illegalArgumentException.message,
            CoreMatchers.containsString("Invalid value for Month Of Year: 13")
        )
    }

    @Test
    fun `test incorrect year in of() parameters`() {
        val illegalArgumentException =
            assertThrows(IllegalArgumentException::class.java) {
                LocalDate.of(25, 3, 2101)
            }
        assertThat(
            illegalArgumentException.message,
            CoreMatchers.containsString("Invalid year value")
        )
    }

    @Test
    fun `test daysInYear`() {
        val hoursValueExpected = 366
        val dateActual = LocalDate.of(31,12,2000).ofYearDay()

        Assert.assertEquals(hoursValueExpected, dateActual)
    }
}