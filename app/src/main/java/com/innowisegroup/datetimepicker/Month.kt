package com.innowisegroup.datetimepicker

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

    fun of(month: Int): Month =
            if (month in 1..12) {
                values()[month - 1]
            } else {
                throw Exception("Invalid value for Month Of Year: $month")
            }

    fun of(): Int =
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
                    else -> 12
                }
            } else {
                throw Exception("Invalid value for number of month: $this")
            }

    fun length(leapYear: Boolean): Int =
            when (this) {
                FEBRUARY -> if (leapYear) 29 else 28
                APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30
                else -> 31
            }
}