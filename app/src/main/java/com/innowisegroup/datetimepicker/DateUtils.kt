package com.innowisegroup.datetimepicker

import android.text.TextUtils
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAccessor

object DateUtils {
    private const val EMPTY = ""
    private val FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm")
    private val FORMAT_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val FORMAT_DATE_AND_TIME = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
    private val FORMAT_DATE_AND_TIME_WITH_MILLIS =
        DateTimeFormatter.ofPattern("HH:mm:ss.SSS dd.MM.yyyy")

    fun formatDateAndTime(localDateTime: TemporalAccessor?): String {
        return if (localDateTime == null) {
            EMPTY
        } else FORMAT_DATE_AND_TIME.format(localDateTime)
    }

    @JvmStatic
    fun formatTime(localTime: TemporalAccessor?): String {
        return if (localTime == null) {
            EMPTY
        } else FORMAT_TIME.format(localTime)
    }

    @JvmStatic
    fun formatDate(localDate: TemporalAccessor?): String {
        return if (localDate == null) {
            EMPTY
        } else FORMAT_DATE.format(localDate)
    }

    fun formatDateAndTimeWithMillis(localDateTime: TemporalAccessor?): String {
        return if (localDateTime == null) {
            EMPTY
        } else FORMAT_DATE_AND_TIME_WITH_MILLIS.format(localDateTime)
    }

    fun formatDateAndTime(dateTime: String?): LocalDateTime? {
        return if (TextUtils.isEmpty(dateTime)) {
            null
        } else LocalDateTime.parse(dateTime, FORMAT_DATE_AND_TIME)
    }

    fun formatDate(date: String?): LocalDateTime? {
        return if (TextUtils.isEmpty(date)) {
            null
        } else LocalDate.parse(date, FORMAT_DATE).atStartOfDay()
    }

    fun formatTime(time: String?): LocalDateTime? {
        return if (TextUtils.isEmpty(time)) {
            null
        } else LocalDateTime.of(LocalDate.now(), LocalTime.parse(time, FORMAT_TIME))
    }
}
