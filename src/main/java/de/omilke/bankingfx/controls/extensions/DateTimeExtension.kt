package de.omilke.bankingfx.controls.extensions

import java.time.LocalDate
import java.time.YearMonth

fun LocalDate.asYearMonth(): YearMonth {

    return YearMonth.of(this.year, this.month)
}

fun LocalDate.atStartOfMonth(): LocalDate {

    return LocalDate.of(this.year, this.month, 1)
}

fun YearMonth.atStartOfMonth(): LocalDate {

    return LocalDate.of(this.year, this.month, 1)
}