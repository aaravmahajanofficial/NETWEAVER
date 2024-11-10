package com.example.netweaver.domain.model

import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

data class Education(
    val id: String = "",
    val userId: String = "",
    val school: String = "",
    val degree: String = "",
    val field: String = "",
    val startDate: Instant = now(),
    val endDate: Instant = now()
)