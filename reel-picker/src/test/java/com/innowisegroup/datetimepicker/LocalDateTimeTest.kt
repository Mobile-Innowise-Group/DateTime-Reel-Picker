package com.innowisegroup.datetimepicker

import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.datetime.LocalDateTime.Companion.of
import com.innowisegroup.reelpicker.datetime.LocalTime
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Test
import java.util.*

class LocalDateTimeTest {

    @Test
    fun `test now() for time`() {
        val timeExpected = LocalTime.now()
        val timeActual = LocalDateTime.now().toLocalTime()

        Assert.assertEquals(timeExpected, timeActual)
    }

    @Test
    fun `test now() for date`() {
        val dateExpected = LocalDate.now()
        val dateActual = LocalDateTime.now().toLocalDate()

        Assert.assertEquals(dateExpected, dateActual)
    }

    @Test
    fun `test of() for time`() {
        val calendar: Calendar = Calendar.getInstance()
        val currentTimeExpected = LocalTime.of(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
        val timeExpected = of(LocalDate.of(1, 1, 2001), currentTimeExpected)
        val timeActual = of(LocalDate.of(1, 1, 2001))

        Assert.assertEquals(timeExpected, timeActual)
    }

    @Test
    fun `test of() for date`() {
        val calendar: Calendar = Calendar.getInstance()
        val currentDateExpected = LocalDate.of(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )
        val dateTimeExpected = of(currentDateExpected, LocalTime.of(1, 1))
        val dateTimeActual = of(LocalTime.of(1, 1))

        Assert.assertEquals(
            dateTimeExpected.toLocalDate().day,
            dateTimeActual.toLocalDate().day
        )
        Assert.assertEquals(
            dateTimeExpected.toLocalDate().month,
            dateTimeActual.toLocalDate().month
        )
        Assert.assertEquals(
            dateTimeExpected.toLocalDate().year,
            dateTimeActual.toLocalDate().year
        )
        Assert.assertEquals(
            dateTimeExpected.toLocalTime().hour,
            dateTimeActual.toLocalTime().hour
        )
        Assert.assertEquals(
            dateTimeExpected.toLocalTime().minute,
            dateTimeActual.toLocalTime().minute
        )
    }

    @Test
    fun `test checkValidDate()`() {
        val illegalArgumentException =
            Assert.assertThrows(IllegalArgumentException::class.java) {
                LocalDateTime.checkValidDate(
                    LocalDate.of(1, 1, 2001),
                    LocalDate.of(1, 1, 2001),
                    LocalDate.of(31, 12, 2000)
                )
            }
        MatcherAssert.assertThat(
            illegalArgumentException.message,
            CoreMatchers.containsString("initialLocalDate does not fit min..max range of minLocalDate and maxLocalDate")
        )
    }

    @Test
    fun `test checkValidTime()`() {
        val illegalArgumentException =
            Assert.assertThrows(IllegalArgumentException::class.java) {
                LocalDateTime.checkValidTime(
                    LocalTime.of(1,1),
                    LocalTime.of(1, 1),
                    LocalTime.of(1,0)
                )
            }
        MatcherAssert.assertThat(
            illegalArgumentException.message,
            CoreMatchers.containsString("initialLocalTime does not fit min..max range of minLocalTime and maxLocalTime")
        )
    }
}