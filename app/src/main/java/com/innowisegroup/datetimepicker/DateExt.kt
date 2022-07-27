package com.innowisegroup.datetimepicker

import android.text.TextUtils
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAccessor

private const val EMPTY = ""
private val FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm")
private val FORMAT_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val FORMAT_DATE_AND_TIME = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
private val FORMAT_DATE_AND_TIME_WITH_MILLIS =
    DateTimeFormatter.ofPattern("HH:mm:ss.SSS dd.MM.yyyy")

internal fun TemporalAccessor?.formatDateAndTime(): String =
    if (this == null) {
        EMPTY
    } else FORMAT_DATE_AND_TIME.format(this)

internal fun TemporalAccessor?.formatTime(): String =
    if (this == null) {
        EMPTY
    } else FORMAT_TIME.format(this)

internal fun TemporalAccessor?.formatDate(): String =
    if (this == null) {
        EMPTY
    } else FORMAT_DATE.format(this)

internal fun TemporalAccessor?.formatDateAndTimeWithMillis(): String =
    if (this == null) {
        EMPTY
    } else FORMAT_DATE_AND_TIME_WITH_MILLIS.format(this)

internal fun String?.formatDateAndTime(): LocalDateTime? =
    if (TextUtils.isEmpty(this)) {
        null
    } else LocalDateTime.parse(this, FORMAT_DATE_AND_TIME)

internal fun String?.formatDate(): LocalDateTime? =
    if (TextUtils.isEmpty(this)) {
        null
    } else LocalDate.parse(this, FORMAT_DATE).atStartOfDay()

internal fun String?.formatTime(): LocalDateTime? =
    if (TextUtils.isEmpty(this)) {
        null
    } else LocalDateTime.of(LocalDate.now(), LocalTime.parse(this, FORMAT_TIME))

