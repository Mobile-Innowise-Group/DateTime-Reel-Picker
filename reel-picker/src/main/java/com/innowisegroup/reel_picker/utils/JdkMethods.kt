package com.innowisegroup.reel_picker.utils

internal fun <T> requireNonNull(value: T?): T =
    value ?: throw NullPointerException("Value must not be null")

internal fun <T> requireNonNull(value: T?, parameterName: String): T =
    value ?: throw NullPointerException("$parameterName must not be null")

internal fun isLeapYear(currentYear: Long): Boolean =
    currentYear and 3L == 0L && (currentYear % 100L != 0L || currentYear % 400L == 0L)