package com.innowisegroup.reelpicker.datetime

enum class Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    companion object {
        internal fun Int.of(): Month =
            if (this in 1..12) {
                values()[this - 1]
            } else {
                throw IllegalArgumentException("Invalid value for Month Of Year: $this")
            }
    }

    internal fun of(): Int =
        if (this in values()) {
            when (this) {
                JANUARY -> 1
                FEBRUARY -> 2
                MARCH -> 3
                APRIL -> 4
                MAY -> 5
                JUNE -> 6
                JULY -> 7
                AUGUST -> 8
                SEPTEMBER -> 9
                OCTOBER -> 10
                NOVEMBER -> 11
                DECEMBER -> 12
            }
        } else {
            throw IllegalArgumentException("Invalid value for number of month: $this")
        }

    internal fun length(leapYear: Boolean): Int =
        when (this) {
            FEBRUARY -> if (leapYear) 29 else 28
            APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30
            else -> 31
        }
}