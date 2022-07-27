package com.innowisegroup.datetimepicker

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAccessor

private val FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm")
private val FORMAT_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val FORMAT_DATE_AND_TIME = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
private val FORMAT_DATE_AND_TIME_WITH_MILLIS =
    DateTimeFormatter.ofPattern("HH:mm:ss.SSS dd.MM.yyyy")

internal fun TemporalAccessor.formatDateAndTime(): String = FORMAT_DATE_AND_TIME.format(this)

internal fun TemporalAccessor.formatTime(): String = FORMAT_TIME.format(this)

internal fun TemporalAccessor.formatDate(): String = FORMAT_DATE.format(this)

internal fun TemporalAccessor.formatDateAndTimeWithMillis(): String =
    FORMAT_DATE_AND_TIME_WITH_MILLIS.format(this)

internal fun String.formatDateAndTime(): LocalDateTime? =
    LocalDateTime.parse(this, FORMAT_DATE_AND_TIME)

internal fun String.formatDate(): LocalDateTime? = LocalDate.parse(this, FORMAT_DATE).atStartOfDay()

internal fun String.formatTime(): LocalDateTime? =
    LocalDateTime.of(LocalDate.now(), LocalTime.parse(this, FORMAT_TIME))