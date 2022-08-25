package com.innowisegroup.datetimepicker

import com.innowisegroup.reelpicker.datetime.LocalTime
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test

class LocaleTimeTest {

    @Test
    fun `test plusMinutes() in case of less than 24x60 min added`() {
        val timeExpected = LocalTime.of(8, 23)
        val timeActual: LocalTime = LocalTime.of(6, 13).plusMinutes(2 * 60 + 10)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test plusMinutes() in case of more than 24hx60 min added`() {
        val timeExpected = LocalTime.of(7, 30)
        val timeActual: LocalTime = LocalTime.of(6, 13).plusMinutes(25 * 60 + 17)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test plusHours() in case of less than 24h added`() {
        val timeExpected = LocalTime.of(16, 30)
        val timeActual: LocalTime = LocalTime.of(6, 30).plusHours(10)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test plusHours() in case of more than 24h added`() {
        val timeExpected = LocalTime.of(5, 30)
        val timeActual: LocalTime = LocalTime.of(6, 30).plusHours(23)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test minusHours() in case of less than 24h added`() {
        val timeExpected = LocalTime.of(19, 30)
        val timeActual: LocalTime = LocalTime.of(6, 30).minusHours(11)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test minusHours() in case of more than 24h added`() {
        val timeExpected = LocalTime.of(22, 30)
        val timeActual: LocalTime = LocalTime.of(6, 30).minusHours(32)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test minusMinutes() in case of less than 24x60 min added`() {
        val timeExpected = LocalTime.of(3, 8)
        val timeActual: LocalTime = LocalTime.of(6, 13).minusMinutes(3 * 60 + 5)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test minusMinutes() in case of more than 24hx60 min added`() {
        val timeExpected = LocalTime.of(5, 0)
        val timeActual: LocalTime = LocalTime.of(6, 13).minusMinutes(25 * 60 + 13)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test withHours() in case of current hour`() {
        val timeExpected = LocalTime.of(12, 20)
        val timeActual: LocalTime = LocalTime.of(12, 20).withHour(12)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test withMinutes() in case of current minute`() {
        val timeExpected = LocalTime.of(12, 20)
        val timeActual: LocalTime = LocalTime.of(12, 20).withMinute(20)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test withHours() in case of different hours`() {
        val timeExpected = LocalTime.of(15, 30)
        val timeActual: LocalTime = LocalTime.of(12, 30).withHour(15)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test withMinutes() in case of different minutes`() {
        val timeExpected = LocalTime.of(15, 33)
        val timeActual: LocalTime = LocalTime.of(15, 15).withMinute(33)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test getSecondsOfTime()`() {
        val timeExpected = 51600
        val timeActual = LocalTime.of(14, 20).getSecondsOfTime()

        Assert.assertEquals(timeExpected, timeActual)
    }

    @Test
    fun `test of()`() {
        val timeExpected = LocalTime.of(5, 15)
        val timeActual: LocalTime = LocalTime.of(5, 15)

        Assert.assertEquals(timeExpected.hour, timeActual.hour)
        Assert.assertEquals(timeExpected.minute, timeActual.minute)
    }

    @Test
    fun `test incorrect hour in of() parameters`() {
        val illegalArgumentException = assertThrows(IllegalArgumentException::class.java) {
            LocalTime.of(25, 1)
        }
        assertThat(illegalArgumentException.message, containsString("Invalid hours value"))
    }

    @Test
    fun `test incorrect minute in of() parameters`() {
        val illegalArgumentException = assertThrows(IllegalArgumentException::class.java) {
            LocalTime.of(11, -1)
        }
        assertThat(illegalArgumentException.message, containsString("Invalid minutes value"))
    }
}