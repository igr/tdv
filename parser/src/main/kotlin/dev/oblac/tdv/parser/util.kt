package dev.oblac.tdv.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun parseDateTime(dateTimeString: String) =
    LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
