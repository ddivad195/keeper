package me.ddivad.keeper.utilities

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun timeToString(milliseconds: Long): String{
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    val hours = (milliseconds / (1000 * 60 * 60)) % 24
    val days = (milliseconds / (1000 * 60 * 60 * 24))
    return ("$days days, $hours hours, $minutes minutes, $seconds seconds")
}

fun formatDate(date: Instant): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK).withZone(ZoneOffset.UTC)
    return formatter.format(date)
}