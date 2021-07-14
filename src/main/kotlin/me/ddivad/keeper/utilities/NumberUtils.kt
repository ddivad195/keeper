package me.ddivad.keeper.utilities

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun formatDate(date: Instant): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK).withZone(ZoneOffset.UTC)
    return formatter.format(date)
}