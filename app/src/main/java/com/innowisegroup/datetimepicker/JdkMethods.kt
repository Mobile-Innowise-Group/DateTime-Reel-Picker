package com.innowisegroup.datetimepicker

fun <T> requireNonNull(value: T?): T =
        value ?: throw NullPointerException("Value must not be null")

fun <T> requireNonNull(value: T?, parameterName: String): T =
        value ?: throw NullPointerException("$parameterName must not be null")

fun isLeapYear(currentYear: Long): Boolean =
        currentYear and 3L == 0L && (currentYear % 100L != 0L || currentYear % 400L == 0L)