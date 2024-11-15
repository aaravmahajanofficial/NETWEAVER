package com.example.netweaver.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun formatTimestampToRelative(timestamp: String): String {

    val dateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.systemDefault())
    val nowDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())

    val years = ChronoUnit.YEARS.between(dateTime, nowDateTime)
    if (years > 0) return "${years}yr"
    val months = ChronoUnit.MONTHS.between(dateTime, nowDateTime)
    if (months > 0) return "${months}mo"
    val days = ChronoUnit.DAYS.between(dateTime, nowDateTime)
    if (days > 0) return "${days}d"
    val week = ChronoUnit.WEEKS.between(dateTime, nowDateTime)
    if (week > 0) return "${week}w"
    val hours = ChronoUnit.HOURS.between(dateTime, nowDateTime)
    if (hours > 0) return "${hours}h"
    val minutes = ChronoUnit.MINUTES.between(dateTime, nowDateTime)
    if (minutes > 0) return "${minutes}m"
    return "now"
}